package com.madmath.core.expression;

import java.util.Random;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 5/12/2021 下午11:29
 */
public class Add implements Expression{
    int A;
    int B;

    public Add(int a,int b){
        A = a;
        B = b;
    }

    public Add(int bound){
        this(new Random().nextInt(bound)+1, new Random().nextInt(bound)+1);
    }

    public Add(){
        this(9);
    }

    @Override
    public int getValue() {
        return A+B;
    }

    @Override
    public String toString() {
        return A + "+" + B ;
    }
}
