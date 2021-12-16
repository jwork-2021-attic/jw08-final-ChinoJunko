package com.madmath.core.entity.obstacle;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.animation.AnimationManager;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 6/12/2021 下午2:21
 */
public class RedFountain extends Obstacle {

    static public int oWidth = 16;

    static public int oHeight = 48;

    static public String alias = "wall_fountain_red_anim";

    static public float frameInterval = 0.2f;

    public RedFountain(AnimationManager animationManager) {
        super(animationManager);
    }

    @Override
    public void initSelfBox() {
        super.initSelfBox();
        box = new Rectangle(0,0,16,16);
        boxOffset = new Vector2(0,12);
    }
}
