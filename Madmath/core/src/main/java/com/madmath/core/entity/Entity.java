/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:56
*/
package com.madmath.core.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.map.TrapTile;
import com.madmath.core.util.Utils;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.myPair;


public class Entity extends AnimationActor {


    public GameScreen gameScreen;

    protected Entity(AnimationManager animationManager){
        this(animationManager,GameScreen.getCurrencyGameScreen());
    }

    public Entity(AnimationManager animationManager, GameScreen gameScreen, Vector2 position){
        super(animationManager);
        this.gameScreen = gameScreen;
        setPosition(position);
        initSelf();
    }

    public Entity(AnimationManager animationManager, GameScreen gameScreen){
        this(animationManager,gameScreen,new Vector2(0,0));
    }

    public void initSelf() {
    }





}
