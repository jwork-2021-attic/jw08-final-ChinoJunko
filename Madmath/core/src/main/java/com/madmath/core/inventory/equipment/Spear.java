package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Cst;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class Spear extends Equipment{

    static public String alias = "weapon_spear";

    public Spear(Integer id, Equipment equipment) {
        super(id, equipment);
    }

    public Spear(Integer id, TextureRegion textureRegion, Sound sound) {
        super(id, textureRegion, sound);
    }


    @Override
    public boolean use() {
        if(isSwinging())    return false;
        if(anim_dirt) rotateBy(90);
        else rotateBy(-90);
        return super.use();
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 30;
        swingSpeed = 30;
        knockbackFactor = 20;
        expression = new Cst(77);
        name = "SevenSword";
        text[0] = "Constant 77";
        text[1] = "Seven is a nice number. A week has 7 day...";
        attackCheckCount = 3;
    }
}
