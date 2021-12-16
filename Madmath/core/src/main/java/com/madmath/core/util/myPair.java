package com.madmath.core.util;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午11:40
 */
public class myPair {
    public TiledMapTile A;
    public Vector2 B;
    myPair(){

    }

    public myPair(TiledMapTile A, Vector2 B){
        this.A = A;
        this.B = B;
    }
}
