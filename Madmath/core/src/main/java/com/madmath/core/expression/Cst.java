package com.madmath.core.expression;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 5/12/2021 下午11:29
 */
public class Cst extends AbstarctExpression{
    int A;

    public Cst(int a){
        A = a;
    }

    @Override
    public int eval(int B) {
        result = A;
        return result;
    }

    @Override
    public String toString(int B) {
        return "-"+ A;
    }
}
