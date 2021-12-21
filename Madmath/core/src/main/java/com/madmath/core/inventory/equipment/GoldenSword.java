package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Mul;
import com.madmath.core.expression.Sub;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class GoldenSword extends Equipment{

    static public String alias = "weapon_lavish_sword";

    public GoldenSword(Integer id, Equipment equipment) {
        super(id, equipment);
    }

    public GoldenSword(Integer id, TextureRegion textureRegion, Sound sound) {
        super(id, textureRegion, sound);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 270;
        swingSpeed = 800;
        knockbackFactor = 3;
        expression = new Mul(0.618f);
        name = "GoldenSword";
        text[0] = "Mul 0.618 per time";
        text[1] = "The Last Goldder.";
        attackCheckCount = 3;
    }
}
