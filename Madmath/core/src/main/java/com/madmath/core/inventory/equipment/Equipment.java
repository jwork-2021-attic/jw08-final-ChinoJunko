/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.entity.creature.Creature;
import com.madmath.core.entity.Entity;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.expression.Expression;
import com.madmath.core.inventory.Item;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Equipment extends Item {

    protected float swingRange;
    protected float swingSpeed;
    protected float currencySwingRange = 0;

    public Color color;

    private Sound sound;

    public Player owner = null;

    public int damage;
    public int knockbackFactor;

    static public String alias;

    public static Array<Equipment> equipmentSort;

    public Array<Entity> attackedTargets = new Array<>();;

    Rectangle pickBox;
    Circle attackCircle;

    TextureRegion textureRegionForClone;

    public Equipment(Integer id, Equipment equipment){
        this(id, equipment.getTextureRegionForClone(),equipment.sound);
    }

    public Equipment(Integer id, TextureRegion region, Sound sound){
        super(new AnimationManager(region,1f));
        initSelf();
        this.id = id;
        this.sound = sound;
        textureRegionForClone = region;
        setRotation(-45);
        addAction(Actions.forever(Actions.sequence(Actions.moveBy(0,8,1.8f),Actions.moveBy(0,-8,1.8f))));
    }

    public int getDamage() {
        return damage;
    }

    public void initSelf(){
        pickBox = new Rectangle(0,0,40,40);
        attackCircle = new Circle(0,0,getHeight());
        swingRange = 220;
        swingSpeed = 500;
        damage = 50;
        color = this.getColor().cpy();
    }

    @Override
    public void setPosition(float x, float y) {
        if(owner==null){
            pickBox.setCenter(new Vector2(x,y));
            super.setPosition(x, y);
        } else {
            attackCircle.setPosition(anim_dirt?x-owner.box.getWidth():x, y);
            super.setPosition(anim_dirt?x-owner.box.getWidth():x, y);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(owner!=null){
            setPosition(owner.getCenterX(),owner.getCenterY());
        }
        if(currencySwingRange>0){
            float swing = swingSpeed * delta;
            currencySwingRange -= swing;
            rotateBy(anim_dirt?swing:-swing);
        }
    }

    @Override
    public Vector2 getItemPosition() {
        return getPosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public boolean canAttack(Expression expression){
        return true;
    }

    @Override
    public boolean canPickUp(Vector2 position) {
        return owner==null&&pickBox.contains(position);
    }

    @Override
    public boolean use() {
        if(isSwinging())    return false;
        attackedTargets.clear();
        currencySwingRange = swingRange;
        if(sound!=null)sound.play();
        Runnable attack = () -> {
            Vector2[] vector2s = new Vector2[4];
            for (int i = 0; i < 4; i++) {
                vector2s[i] = new Vector2();
            }
            try {
                for (int i = 0; i < owner.gameScreen.map.livingEntity.size; i++) {
                    try {
                        if (owner.gameScreen.map.livingEntity.get(i)instanceof Monster && !attackedTargets.contains(Objects.requireNonNull(owner.gameScreen.map.livingEntity.get(i)), true)) {
                            attackCircle.radius += 50;
                            boolean temp = attackCircle.contains(Objects.requireNonNull(owner.gameScreen.map.livingEntity.get(i)).box.getCenter(vector2s[0]));
                            attackCircle.radius -= 50;
                            Creature creature =(Creature) owner.gameScreen.map.livingEntity.get(i);
                            if (!temp) continue;
                            if (Math.abs(getRotation() % 360 - vector2s[0].sub(owner.box.getCenter(vector2s[1])).angle()) > swingRange)
                                continue;
                            for (int j = 0; j < 4; j++) {
                                Objects.requireNonNull(creature).box.getPosition(vector2s[j]);
                            }
                            vector2s[1].add(Objects.requireNonNull(creature).box.getWidth(), 0);
                            vector2s[2].add(0, Objects.requireNonNull(creature).box.getHeight());
                            vector2s[3].add(Objects.requireNonNull(creature).box.getWidth(), Objects.requireNonNull(creature).box.getHeight());
                            for (int j = 0; j < 4; j++) {
                                if (attackCircle.contains(vector2s[j])) {
                                    attackedTargets.add(Objects.requireNonNull(creature));
                                    owner.gameScreen.getGame().manager.punchSound.get(j).play();
                                    Objects.requireNonNull(creature).getHurt(this);
                                    //System.out.println("Attack!");
                                    continue;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        continue;
                    }
                }
            } finally {
                return;
            }
        };
        float perAttackCheckInterval = 2f/ Gdx.graphics.getFramesPerSecond();
        float perAttackMakeInterval = 0.2f;

        //int totalAttackNum = (int) (swingRange/swingSpeed/perAttackCheckInterval);
        Runnable attackClear = ()-> attackedTargets.clear();
        Action a = Actions.forever(Actions.sequence(Actions.delay(perAttackMakeInterval),Actions.run(attackClear)));
        Action b = Actions.forever(Actions.sequence(Actions.delay(perAttackCheckInterval),Actions.run(attack)));
        addAction(Actions.sequence(
                Actions.addAction(Actions.sequence(Actions.delay(swingRange/swingSpeed),Actions.removeAction(a),Actions.removeAction(b))),
                Actions.addAction(a),
                Actions.addAction(b)
        ));
        return true;
    }

    public int getKnockbackFactor() {
        return knockbackFactor;
    }

    public void equippedBy(Player player){
        clearActions();
        owner = player;
    }

    public void beThrown(){
        owner.gameScreen.map.livingItem.add(this);
        owner = null;
        setPosition(getX(),getY());
    }

    public boolean isSwinging(){
        return currencySwingRange > 0;
    }

    @Override
    public void setRotation(float degrees) {
        if(!isSwinging()){
            int kAngle = Math.round(degrees) % 360;
            anim_dirt = kAngle > 90 && kAngle <= 270;
            setOrigin(anim_dirt?getWidth():0,0);
            super.setRotation(anim_dirt? degrees-180:degrees);
        }
    }

    public TextureRegion getTextureRegionForClone(){
        return textureRegionForClone;
    }

    public Equipment copy(){
        return GameScreen.getCurrencyGameScreen().getEquipmentFactory().generateEquipmentByName(getClass().getName());
    }

    static public void loadEquipment(){
        equipmentSort = new Array<>();
        for (String name: Utils.AllDefaultEquipmentSort) {
            try {
                Class<?> c = Class.forName("com.madmath.core.inventory.equipment."+name);
                Constructor<?> con = c.getConstructor(Integer.class, TextureRegion.class, Sound.class);
                equipmentSort.add((Equipment) con.newInstance(300+Equipment.equipmentSort.size,ResourceManager.defaultManager.LoadEquipmentAssetsByName((String) c.getField("alias").get(null)), ResourceManager.defaultManager.getSoundByName((String) c.getField("alias").get(null))));
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Equipment Named '" +name+'\'');
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
    }

}
