package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Add;
import com.madmath.core.expression.Cst;
import com.madmath.core.expression.Expression;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class SevenSword extends Equipment{

    static public String alias = "weapon_anime_sword";

    public SevenSword(Integer id, Equipment equipment) {
        super(id, equipment);
    }

    public SevenSword(Integer id, TextureRegion textureRegion, Sound sound) {
        super(id, textureRegion, sound);
    }


    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 205;
        swingSpeed = 410;
        knockbackFactor = 20;
        expression = new Cst(77);
        name = "SevenSword";
        text[0] = "Constant 77";
        text[1] = "Seven is a nice number. A week has 7 day...";
        attackCheckCount = 1;
    }
}
