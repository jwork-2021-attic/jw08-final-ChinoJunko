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

public class Ogre extends Monster{

    static public String alias = "ogre";

    static public int TextureWidth = 32;
    static public int TextureHeight = 32;
    //default frameInterval :
    //    static public float[] FrameInterval = {
    //            0.34f,          //Idle
    //            0,17f,          //Run
    //    };

    public Ogre(AnimationManager manager){
        super(manager);
    }

    public Ogre(Integer id, AnimationManager manager){
        super(id, manager);
    }

    public Ogre(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    public Ogre(Integer id, AnimationManager animationManager, GameScreen gameScreen) {
        super(id, animationManager, gameScreen);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        box = new Rectangle(0,0,22,10);
        boxOffset = new Vector2(5,0);
        inertia = 0.06f;
        speed = 20f;
        level = 4;
        maxHp = 720;
        hp = 720;
        toughness = 0.6f;
    }

}
