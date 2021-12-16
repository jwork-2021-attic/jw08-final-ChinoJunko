package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public boolean canAttack(Expression expression) {
        return expression.getValue()%7==0;
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 360;
        swingSpeed = 410;
        knockbackFactor = 20;
        damage = 100;
    }
}
