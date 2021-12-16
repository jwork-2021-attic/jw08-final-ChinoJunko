package com.madmath.core.map;

import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.entity.Entity;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午11:14
 */
public class TrapTile extends AnimTile{

    float knockbackFactor;

    long activeFrameCount;

    public TrapTile(float interval, Array<StaticTiledMapTile> frameTiles) {
        super(interval, frameTiles);
        knockbackFactor = 0.5f;
        activeFrameCount = (frameTiles.size-4)/2;
    }

    public float getKnockbackFactor() {
        return knockbackFactor;
    }

    public boolean isActive(){
        return getCurrentFrameIndex() >  activeFrameCount/2 + 1 && getCurrentFrameIndex() < getFrameCount()-activeFrameCount/2-2 ;
    }

    public int trigger(Entity entity){
        return 1;
    }

}
