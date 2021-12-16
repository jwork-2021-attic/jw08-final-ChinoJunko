/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:56
*/
package com.madmath.core.entity.creature;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.screen.GameScreen;

public class BigDemon extends Monster{

    static public String alias = "big_demon";

    static public int TextureWidth = 32;
    static public int TextureHeight = 36;
    //default frameInterval :
    //    static public float[] FrameInterval = {
    //            0.34f,          //Idle
    //            0,17f,          //Run
    //    };

    public BigDemon(AnimationManager manager){
        super(manager);
    }

    public BigDemon(Integer id, AnimationManager manager){
        super(id, manager);
    }

    public BigDemon(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    public BigDemon(Integer id, AnimationManager animationManager, GameScreen gameScreen) {
        super(id, animationManager, gameScreen);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        box = new Rectangle(0,0,22,10);
        boxOffset = new Vector2(5,0);
        inertia = 0.03f;
        speed = 36f;
        damage = 7;
        level = 10;
        maxHp = 5000;
        hp = 5000;
        toughness = 0.5f;
    }

}
