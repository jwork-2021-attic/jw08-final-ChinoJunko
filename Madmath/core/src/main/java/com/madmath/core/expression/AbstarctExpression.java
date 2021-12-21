package com.madmath.core.expression;

import com.badlogic.gdx.graphics.Color;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 21/12/2021 上午9:28
 */
public abstract class AbstarctExpression implements Expression{
    public int result;

    @Override
    public int getResult() {
        return result;
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }
}
