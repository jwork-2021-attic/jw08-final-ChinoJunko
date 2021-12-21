package com.madmath.core.expression;

import com.badlogic.gdx.graphics.Color;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 5/12/2021 下午11:27
 */
public interface Expression {

    public int getResult();

    public Color getColor();

    public int eval(int B);

    public String toString(int B);
}
