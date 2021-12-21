/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.madmath.core.screen.AbstractScreen;
import com.madmath.core.screen.GameScreen;

public class AnimTile implements TiledMapTile {

    private static long lastTiledMapRenderTime = 0L;
    private int id;
    private BlendMode blendMode;
    private MapProperties properties;
    private Array<StaticTiledMapTile> frameTiles;
    private float animationInterval;
    private long frameCount;
    private static long initialTimeOffset = TimeUtils.millis();

    public static void syncTime(long lastTime){
        long offset = lastTime-lastTiledMapRenderTime;
        lastTiledMapRenderTime += offset;
        initialTimeOffset -= offset;
    }

    public static long getLastTiledMapRenderTime() {
        return lastTiledMapRenderTime;
    }

    public AnimTile(float interval, Array<StaticTiledMapTile> frameTiles) {
        this.blendMode = BlendMode.ALPHA;
        this.frameTiles = frameTiles;
        this.animationInterval = interval;
        this.frameCount = frameTiles.size;
    }

    public long getCurrentFrameIndex()  {
        updateAnimationBaseTime();
        return lastTiledMapRenderTime / (long)(this.animationInterval * 1000.0F) % this.frameCount;
    }

    public long getFrameCount() {
        return frameCount;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BlendMode getBlendMode() {
        return this.blendMode;
    }

    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    public TextureRegion getTextureRegion() {
        return ((StaticTiledMapTile)this.frameTiles.get((int)getCurrentFrameIndex())).getTextureRegion();
    }

    public float getOffsetX() {
        return ((StaticTiledMapTile)this.frameTiles.get((int)getCurrentFrameIndex())).getOffsetX();
    }

    public void setOffsetX(float offsetX) {
        throw new GdxRuntimeException("Cannot set offset of AnimatedTiledMapTile.");
    }

    public float getOffsetY() {
        return ((StaticTiledMapTile)this.frameTiles.get((int)getCurrentFrameIndex())).getOffsetY();
    }

    public void setOffsetY(float offsetY) {
        throw new GdxRuntimeException("Cannot set offset of AnimatedTiledMapTile.");
    }

    public MapProperties getProperties() {
        if (this.properties == null) {
            this.properties = new MapProperties();
        }

        return this.properties;
    }

    public static void updateAnimationBaseTime() {
        Long Delta = TimeUtils.millis()-lastTiledMapRenderTime-initialTimeOffset;
        if(GameScreen.getCurrencyGameScreen().getState()!= AbstractScreen.State.RUNING) {
            initialTimeOffset += Delta;
        }
        else {
            lastTiledMapRenderTime += Delta;
        }
    }

    public enum TileSort{
        floor_spikes_anim
    };
}
