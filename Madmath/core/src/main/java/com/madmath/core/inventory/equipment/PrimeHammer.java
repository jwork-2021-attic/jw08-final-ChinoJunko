package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Expression;
import com.madmath.core.expression.Mul;

import java.util.HashSet;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class PrimeHammer extends Equipment{

    static public String alias = "weapon_big_hammer";

    public PrimeHammer(Integer id, Equipment equipment) {
        super(id, equipment);
    }

    public PrimeHammer(Integer id, TextureRegion textureRegion, Sound sound) {
        super(id, textureRegion, sound);
    }

    private HashSet<Integer> Prime;


    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 4800;
        swingSpeed = 2400;
        knockbackFactor = 12;
        initPrime();
        expression = new Mul(1.01f);
        name = "PrimeHammer";
        text[0] = "Mul by 1.01";
        text[1] = "Small Changes Make Big Impact";
        attackCheckCount = 10;
    }

    private void initPrime(){
        Prime = new HashSet<>();
        for (int i = 1; i < 10000; i+=2) {
            boolean isPrime = true;
            for (int j = 3; j < i; j+=2) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if(isPrime) Prime.add(i);
        }
    }
}
