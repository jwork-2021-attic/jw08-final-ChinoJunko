/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.mod.necromancer.Monster;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.entity.creature.Monster;

public class NecroMancer extends Monster {

    static public String alias = "necro_mancer";

    static public int TextureWidth = 16;
    static public int TextureHeight = 20;

    public NecroMancer(Integer id, AnimationManager animationManager) {
        super(id, animationManager);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        speed = 22f;
        maxHp = 100;
        hp = 100;
        box = new Rectangle(0,0,12,5);
        boxOffset = new Vector2(2,0);
        level = 2;
    }
}
