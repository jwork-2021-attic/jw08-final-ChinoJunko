package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Expression;

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
    public boolean canAttack(Expression expression) {
        return expression.getValue()%2==0;
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 220;
        swingSpeed = 500;
        knockbackFactor = 6;
        damage = 50;
    }
}
