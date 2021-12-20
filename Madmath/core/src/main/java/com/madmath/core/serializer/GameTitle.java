package com.madmath.core.serializer;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午6:03
 */
public class GameTitle {
    public String name;
    public int mapLevel;
    public float factor;
    public int score;

    public GameTitle(String name,int mapLevel,float factor,int score){
        this.name = name;
        this.mapLevel = mapLevel;
        this.factor = factor;
        this.score = score;
    }
}
