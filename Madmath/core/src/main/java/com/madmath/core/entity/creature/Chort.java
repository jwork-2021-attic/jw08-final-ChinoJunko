/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午11:06
*/
package com.madmath.core.entity.creature;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.screen.GameScreen;

public class Chort extends Monster{

    static public String alias = "chort";

    static public int TextureWidth = 16;
    static public int TextureHeight = 24;
    //default frameInterval :
    //    static public float[] FrameInterval = {
    //            0.34f,          //Idle
    //            0,17f,          //Run
    //    };

    public Chort(AnimationManager manager){
        super(manager);
    }

    public Chort(Integer id, AnimationManager manager){
        super(id, manager);
    }

    public Chort(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    public Chort(Integer id, AnimationManager animationManager, GameScreen gameScreen) {
        super(id, animationManager, gameScreen);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        box = new Rectangle(0,0,12,8);
        boxOffset = new Vector2(2,0);
        inertia = 0.13f;
        speed = 32f;
        damage = 3;
        level = 3;
        maxHp = 200;
        hp = 200;
    }

}
