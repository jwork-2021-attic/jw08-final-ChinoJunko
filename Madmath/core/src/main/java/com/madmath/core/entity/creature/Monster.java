/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.entity.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.animation.CustomAnimation;
import com.madmath.core.expression.Add;
import com.madmath.core.expression.Damage;
import com.madmath.core.expression.Expression;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Monster extends Creature{

    static public String alias;

    public static Array<Monster> monsterSort;

    public int level;

    protected float knockback;

    Expression expression;
    Label label;

    static public int TextureWidth;
    static public int TextureHeight;
    static public float[] FrameIntervals = {
            0.34f,          //Idle
            0.17f,           //Run
    };

    protected int damage;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //label.setCenterPosition(getCenterX(),getY()+getHeight()+label.getHeight());
        //label.draw(batch, parentAlpha);
    }

    protected Monster(AnimationManager animationManager){
        super(animationManager);
    }

    protected Monster(Integer id, AnimationManager animationManager){
        super(id, animationManager);
    }

    public Monster(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    public Monster(Integer id, AnimationManager animationManager, GameScreen gameScreen) {
        super(id, animationManager, gameScreen);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        knockback = 0.5f;
        damage = 1;
        expression = new Add();
        //label = new Label("",new Label.LabelStyle(ResourceManager.defaultManager.font,Color.YELLOW));
        //label.setFontScale((getWidth()+getHeight()+100)/600f);
        //label.setText(expression.toString());
        //label.setVisible(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void monsterAct(float delta){
        AtomicReference<Player> player = new AtomicReference<>(gameScreen.player);
        AtomicReference<Float> distance = new AtomicReference<>(getPosition().dst2(player.get().getPosition()));
        gameScreen.teammate.forEach((id,mate)->{
            float tempDistance = getPosition().dst2(mate.getPosition());
            if(distance.get() >tempDistance){
                player.set(mate);
                distance.set(tempDistance);
            }
        });
        if(distance.get() >90000f) return;
        Vector2 direction = new Vector2(player.get().getX()-getX(), player.get().getY()-getY());
        direction.x = Math.abs(direction.x)< player.get().getWidth()/2?0:direction.x>0?inertia:-inertia;
        direction.y = Math.abs(direction.y)< player.get().getHeight()/2?0:direction.y>0?inertia:-inertia;
        addAcceleration(direction);
        move(delta);
        attack(player.get());
    }

    @Override
    public int getHurt(Equipment equipment) {
        //if(!equipment.canAttack(expression)||hp<=0)  return 0;
        if(hp<=0) return 0;
        new Damage(this,equipment.getDamage());
        return super.getHurt(equipment);
    }

    @Override
    public void Die() {
        super.Die();
        gameScreen.player.score+=(level*50000+gameScreen.map.mapLevel)*gameScreen.map.difficultyFactor;
        //gameScreen.getStage().getActors().removeValue(this,true);
        //gameScreen.getStage().getActors().removeValue(label,true);
        gameScreen.monsterManager.removeMonster(this);
        addAction(Actions.sequence(Actions.addAction(Actions.forever(Actions.sequence(Actions.run(()->{addAccelerationOverSpeed(new Vector2(0,0));move(1f/Gdx.graphics.getFramesPerSecond());}),Actions.delay(1f/Gdx.graphics.getFramesPerSecond())))),Actions.color(Color.RED.cpy()),Actions.addAction(Actions.color(Color.WHITE.cpy(),0.5f)),Actions.fadeOut(1f),Actions.delay(1f),Actions.run(()->{
            gameScreen.map.livingEntity.removeValue(this,true);
            gameScreen.getStage().getActors().removeValue(this,true);
        })));

    }

    public int getDamage() {
        return damage;
    }

    public float getKnockback() {
        return knockback;
    }

    public void attack(Player player){
        Rectangle attackBox = new Rectangle(0,0,box.getWidth()+10,box.getHeight()+10).setCenter(box.getCenter(new Vector2()));
        if(attackBox.overlaps(gameScreen.player.box)){
            player.getHurt(this);
        }
    }

    public Monster clone(int id){
        try {
            return getClass().getConstructor(Integer.class,AnimationManager.class).newInstance(id,getAnimationManager().clone());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } return null;
    }

    static public void loadMonsters(){
        searchModMonster();
        monsterSort = new Array<>();
        for (String name: Utils.AllDefaultMonsterSort) {
            try {
                Class<?> c = Class.forName("com.madmath.core.entity.creature."+name);
                Constructor<?> con = c.getConstructor(Integer.class,AnimationManager.class);
                monsterSort.add((Monster) con.newInstance(500+ResourceManager.defaultManager.MonsterLoad.size,
                        new AnimationManager(
                        new CustomAnimation(((float[]) c.getField("FrameIntervals").get(null))[0],
                                new Array<>(ResourceManager.defaultManager.LoadMonsterAssetsByName(c.getField("alias").get(null) +"_idle_anim",
                                        (int)c.getField("TextureWidth").get(null),
                                        (int)c.getField("TextureHeight").get(null))[0])),
                        new CustomAnimation(((float[]) c.getField("FrameIntervals").get(null))[1],
                                new Array<>(ResourceManager.defaultManager.LoadMonsterAssetsByName(c.getField("alias").get(null) +"_run_anim",
                                        (int)c.getField("TextureWidth").get(null),
                                        (int)c.getField("TextureHeight").get(null))[0])))));
                System.out.println("Monster Load : "+ name);
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Monster Named '" +name+'\'');
            } catch (NoSuchFieldException e){
                System.out.println("Not Such Field :'" +e.getMessage()+'\'');
            } catch (IllegalAccessException e){
                System.out.println("IllegalAccessField :'" +e.getMessage()+'\'');
            } catch (NoSuchMethodException e){
                System.out.println("Not Such Method :'" +e.getMessage()+'\'');
                e.printStackTrace();
            }
            catch (InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < Utils.ModLoadMonsterPath.size(); i++) {
            try {
                Class<?> c = Class.forName(Utils.ModLoadMonsterPath.get(i));
                Constructor<?> con = c.getConstructor(Integer.class,AnimationManager.class);
                monsterSort.add((Monster) con.newInstance(500+ResourceManager.defaultManager.MonsterLoad.size,
                        new AnimationManager(ResourceManager.defaultManager.LoadMonsterAssetsByPath(Utils.ModLoadMonsterTexture.get(i),
                                                (int)c.getField("TextureWidth").get(null),
                                                (int)c.getField("TextureHeight").get(null)),
                                (float[]) c.getField("FrameIntervals").get(null))));
                //monsterSort.add();
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Monster Path '" +Utils.ModLoadMonsterPath.get(i)+'\'');
            } catch (NoSuchFieldException e){
                System.out.println("Not Such Field :'" +e.getMessage()+'\'');
            } catch (IllegalAccessException e){
                System.out.println("IllegalAccessField :'" +e.getMessage()+'\'');
            } catch (NoSuchMethodException e){
                System.out.println("Not Such Method :'" +e.getMessage()+'\'');
                e.printStackTrace();
            }
            catch (InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            System.out.println("Mod Monster Load : "+ monsterSort.get(monsterSort.size-1));
        }
        monsterSort.sort((monster1,monster2)-> monster2.level - monster1.level);
    }

    public static void searchModMonster(){
        LinkedList<File> list = new LinkedList<>();
        list.add(new File("Madmath/core/src/main/java/com/madmath/core/mod"));
        while (!list.isEmpty()){
            File[] flist = list.pollFirst().listFiles();
            if(flist!=null){
                Arrays.stream(flist).forEach(file -> {
                    if(file.isDirectory())  list.add(file);
                    else {
                        if(file.getParent().substring(file.getParent().lastIndexOf("\\")+1).equals("Monster")){
                            String s = file.getPath().substring(file.getPath().indexOf("com"),file.getPath().lastIndexOf(".java")).replaceAll("\\\\",".");
                            //System.out.println(s);

                            Utils.ModLoadMonsterPath.add(s);
                        }
                        else if(file.getParent().substring(file.getParent().lastIndexOf("\\")+1).equals("Texture")){
                            Utils.ModLoadMonsterTexture.add(file.getPath());
                            //System.out.println(Utils.ModLoadMonsterTexture.getLast().getHeight());
                        }
                    }
                });
            }
        }

        for (String path: Utils.ModLoadMonsterPath
        ) {
            System.out.println("ModLoadMonsterPath:"+path);
        }
        for (String path: Utils.ModLoadMonsterTexture
        ) {
            System.out.println("ModLoadMonsterTexture:"+path);
        }
    }


}
