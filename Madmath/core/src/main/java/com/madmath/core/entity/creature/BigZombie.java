/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午11:02
*/
package com.madmath.core.entity.creature;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.screen.GameScreen;

public class BigZombie extends Monster{

    static public String alias = "big_zombie";

    static public int TextureWidth = 32;
    static public int TextureHeight = 34;
    //default frameInterval :
    //    static public float[] FrameInterval = {
    //            0.34f,          //Idle
    //            0,17f,          //Run
    //    };
    static public float[] FrameIntervals = {
            0.68f,          //Idle
            0.34f,           //Run
    };



    public BigZombie(AnimationManager manager){
        super(manager);
    }

    public BigZombie(Integer id, AnimationManager manager){
        super(id, manager);
    }

    public BigZombie(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    public BigZombie(Integer id, AnimationManager animationManager, GameScreen gameScreen) {
        super(id, animationManager, gameScreen);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        box = new Rectangle(0,0,22,10);
        boxOffset = new Vector2(5,0);
        inertia = 0.05f;
        speed = 17f;
        damage = 6;
        level = 6;
        maxHp = 1000;
        hp = 1000;
        toughness = 0.7f;
        isNeg = true;
    }

}
