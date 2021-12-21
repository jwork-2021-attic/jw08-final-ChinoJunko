package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Expression;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class OddSword extends Equipment{

    static public String alias = "weapon_duel_sword";

    public OddSword(Integer id, Equipment equipment) {
        super(id, equipment);
    }

    public OddSword(Integer id, TextureRegion textureRegion, Sound sound) {
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
        swingSpeed = 300;
        knockbackFactor = 4;
        name = "OddSword";
        text[0] = "Inc 5 per time";
        text[1] = "Slow but firm strides";
    }
}
