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

public class OrcShaman extends Monster{

    static public String alias = "orc_shaman";

    static public int TextureWidth = 16;
    static public int TextureHeight = 20;
    //default frameInterval :
    //    static public float[] FrameInterval = {
    //            0.34f,          //Idle
    //            0,17f,          //Run
    //    };

    public OrcShaman(AnimationManager manager){
        super(manager);
    }

    public OrcShaman(Integer id, AnimationManager manager){
        super(id, manager);
    }

    public OrcShaman(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    public OrcShaman(Integer id, AnimationManager animationManager, GameScreen gameScreen) {
        super(id, animationManager, gameScreen);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        box = new Rectangle(0,0,12,7);
        boxOffset = new Vector2(2,0);
        inertia = 0.1f;
        speed = 20f;
        level = 3;
        maxHp = 200;
        hp = 200;
    }

}
