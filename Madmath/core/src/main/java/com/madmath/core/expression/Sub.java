package com.madmath.core.expression;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 5/12/2021 下午11:29
 */
public class Sub extends AbstarctExpression{
    int A;

    public Sub(int a){
        A = a;
    }

    @Override
    public int eval(int B) {
        result = B - A;
        return result;
    }

    @Override
    public String toString(int B) {
        return "-(" + B + "-" + A + ")";
    }
}
