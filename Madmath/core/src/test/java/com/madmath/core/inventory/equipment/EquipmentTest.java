package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.screen.GameScreen;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 17/12/2021 上午2:41
 */
public class EquipmentTest {

    @Injectable Equipment equipment;

    @Test
    public void getDamage() {
        new Expectations() {
            {
                equipment.getDamage();
                result = equipment.damage;
            }
        };
        int damage = equipment.getDamage();
        Assert.assertTrue(damage==equipment.damage);
    }

    @Test
    public void getItemPosition() {
        new Expectations() {
            {
                equipment.getItemPosition();
                result = Deencapsulation.getField(equipment,"currencyPosition");
            }
        };
        Vector2 vector2 = equipment.getItemPosition();
        Assert.assertTrue(vector2==Deencapsulation.getField(equipment,"currencyPosition"));
    }

    @Test
    public void canAttack() {
    }

    @Test
    public void canPickUp() {
    }

    @Test
    public void getKnockbackFactor() {
    }

    @Test
    public void isSwinging() {
    }

    @Test
    public void getTextureRegionForClone() {
    }

    @Test
    public void copy() {
    }
}