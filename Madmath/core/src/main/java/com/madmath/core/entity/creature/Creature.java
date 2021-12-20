/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:56
*/
package com.madmath.core.entity.creature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.entity.Entity;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.map.TrapTile;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;
import com.madmath.core.util.myPair;


public class Creature extends Entity {


    protected int id;
    protected String name;

    protected boolean pauseAnim = false;


    //positive right and up
    protected Vector2 currentDirection = new Vector2(0,0);

    public float inertia;
    public float toughness;


    //if move set it to 16, reduce by half per frame
    //public int lastMove;

    public boolean canMove = true;

    Color color;
    Color hurtColor;

    protected float speed;
    //protected float lostSpeed;
    //protected Vector2 acceleration=new Vector2(0,0);


    //RPG
    protected int maxHp;
    protected int hp;


    protected Creature(AnimationManager animationManager){
        super(animationManager);
        id = -1;
    }

    protected Creature(Integer id, AnimationManager animationManager){
        super(animationManager,GameScreen.getCurrencyGameScreen());
        this.id = id;
    }

    public Creature(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position){
        super(animationManager,gameScreen,position);
        this.id = id;
    }

    public Creature(Integer id, AnimationManager animationManager, GameScreen gameScreen){
        super(animationManager,gameScreen);
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setAnimDirection(currentDirection.x < 0 || (currentDirection.x == 0 && anim_dirt));
        if(getState()== State.Move){
            setPlayMode(AnimationManager.PlayMode.Moving);
        }else {
            setPlayMode(AnimationManager.PlayMode.Stand);
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public boolean move(float v){
        if(getState()== State.Stand) return false;
        float moveDistance = speed * v;
        Vector2 next1 = new Vector2(getPosition());
        Vector2 next2 = new Vector2(getPosition());
        Vector2 next3 = new Vector2(getPosition());
        next1.x += currentDirection.x*moveDistance;
        next1.y += currentDirection.y*moveDistance;
        next2.x = next1.x;
        next3.y = next1.y;
        if(isCanMove(next1)){
            setPosition(next1);
            return true;
        }else if(isCanMove(next2)){
            setPosition(next2);
            return true;
        }else if(isCanMove(next3)){
            setPosition(next3);
            return true;
        }
        return false;
    }

    @Override
    public void initSelf() {
        super.initSelf();
        speed = 16f;
        maxHp = 1;
        hp = 1;
        inertia = 0.1f;
        toughness = 0f;
        color = this.getColor().cpy();
        hurtColor = Color.RED.cpy();
    }



    public State getState(){
        if(currentDirection.len2()>0.1) return canMove? State.Move: State.Hit;
        return State.Stand;
    }

    public int getHurt(TrapTile tile,Vector2 position){
        return getHurt(tile.trigger(this),new Vector2(position).sub(new Vector2(1,1)),tile.getKnockbackFactor());
    }

    public int getHurt(Equipment equipment){
        return getHurt(equipment.getDamage(),new Vector2(equipment.owner.getPosition()),equipment.getKnockbackFactor());
    }

    public int getHurt(int damage, Vector2 sufferFrom, float knockbackFactor){
        canMove = false;
        addAction(Actions.sequence(Actions.delay(0.4f*(1-toughness)),Actions.run(()->{canMove = true;})));
        knockbackFactor *= (1-toughness);
        addAction(Actions.sequence(Actions.color(Color.RED),Actions.color(color,0.4f)));
        sufferFrom.sub(getPosition());
        sufferFrom.x = sufferFrom.x>0?-knockbackFactor:knockbackFactor;
        sufferFrom.y = sufferFrom.y>0?-knockbackFactor:knockbackFactor;
        setAcceleration(sufferFrom);
        hp -= damage;
        return damage;
    }

    public void Die(){

    }

    public Array<myPair> getTileOnFoot(Vector2 position){
        Array<myPair> tiledMapTileVector2Pair = new Array<>();
        for (float i = position.x; i <= position.x+box.getWidth(); i += box.getWidth()) {
            for (float j = position.y; j <= position.y+box.getHeight(); j += box.getHeight()) {
                TiledMapTile tiles = ((TiledMapTileLayer) gameScreen.getMap().getTiledMap().getLayers().get(0)).getCell((int)i/16,(int)j/16).getTile();
                tiledMapTileVector2Pair.add(new myPair(tiles,new Vector2(i,j)));
            }
        }
        return tiledMapTileVector2Pair;
    }

    public boolean isCanMove(Vector2 next){
        next.add(boxOffset);
        for (float i = next.x; i <= next.x+box.getWidth(); i += box.getWidth()) {
            for (float j = next.y; j <= next.y+box.getHeight(); j += box.getHeight()) {
                if(i< gameScreen.getMap().startPosition.x||i>= gameScreen.getMap().startPosition.x+ gameScreen.getMap().playAreaSize.x||j< gameScreen.getMap().startPosition.y||j>= gameScreen.getMap().startPosition.y+ gameScreen.getMap().playAreaSize.y)  return false;
                TiledMapTileLayer layer =(TiledMapTileLayer) gameScreen.getMap().getTiledMap().getLayers().get(0);
                TiledMapTile tile = layer.getCell((int)i/16,(int)j/16).getTile();
                if(!Utils.accessibleG.contains(tile.getId()))   return false;
            }
        }
        Rectangle nextBox = new Rectangle(box).setPosition(next);
        Vector2 toAcr = next.cpy().sub(boxOffset).sub(getPosition());
        for (int i = 0; i < gameScreen.map.livingEntity.size; i++) {
            if(gameScreen.map.livingEntity.get(i) != this && gameScreen.map.livingEntity.get(i).box.overlaps(nextBox) && !toAcr.hasOppositeDirection(gameScreen.map.livingEntity.get(i).box.getCenter(new Vector2()).sub(box.getCenter(new Vector2()))))  {
                return false;
            }
        }
        return true;
    }

    public void setAnimDirection(boolean direction){
        anim_dirt = (getState() == State.Hit) != direction;
    }

    public void addAcceleration(Vector2 Direction) {
        if(Math.abs(currentDirection.x)>1||Math.abs(currentDirection.y)>1) {
            addAccelerationOverSpeed(Direction);
            return;
        }
        currentDirection.x = Math.min(Math.max(currentDirection.x + Direction.x,-1f),1f);
        currentDirection.y = Math.min(Math.max(currentDirection.y + Direction.y,-1f),1f);
    }

    public void addAccelerationOverSpeed(Vector2 Direction){
        currentDirection.x = (currentDirection.x + Direction.x)*0.9f;
        currentDirection.y = (currentDirection.y + Direction.y)*0.9f;
    }

    public void setAcceleration(Vector2 Direction) {
        currentDirection.set(Direction);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public enum State{
        Stand,
        Move,
        Hit,
    }
}
