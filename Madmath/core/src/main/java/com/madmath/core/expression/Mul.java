package com.madmath.core.expression;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 5/12/2021 下午11:29
 */
public class Mul extends AbstarctExpression{
    Float A;

    public Mul(Float a){
        A = a;
    }

    @Override
    public int eval(int B) {
        result = Math.round(B * A);
        return result;
    }

    @Override
    public String toString(int B) {
        return "-(" + B + "*" + A + ")";
    }
}
