package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Expression;
import com.madmath.core.expression.Sub;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class EvenSword extends Equipment{

    static public String alias = "weapon_knight_sword";

    public EvenSword(Integer id, Equipment equipment) {
        super(id, equipment);
    }

    public EvenSword(Integer id, TextureRegion textureRegion, Sound sound) {
        super(id, textureRegion, sound);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 220;
        swingSpeed = 500;
        knockbackFactor = 6;
        expression = new Sub(5);
        name = "EvenSword";
        text[0] = "Sub 5 per time";
        text[1] = "People treat me as a freak, but they need me after all.";
    }
}
