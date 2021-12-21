package com.madmath.core.expression;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 5/12/2021 下午11:29
 */
public class Add extends AbstarctExpression{
    int A;

    public Add(int a){
        A = a;
    }

    @Override
    public int eval(int B) {
        result = B + A;
        return result;
    }

    @Override
    public String toString(int B) {
        return "-(" + B + "+" + A + ")";
    }
}
