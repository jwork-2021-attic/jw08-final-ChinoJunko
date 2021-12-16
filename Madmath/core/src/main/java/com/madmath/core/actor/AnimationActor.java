/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:56
*/
package com.madmath.core.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.animation.CustomAnimation;

public class AnimationActor extends Image {
    private final AnimationManager animationManager;
    private float stateTime;
    private final Vector2 currencyPosition;
    protected boolean anim_dirt = false;//true - left | false - right

    public Rectangle box;
    public Vector2 boxOffset;

    public AnimationActor(AnimationManager animationManager){
        super(animationManager.getKeyFrame(0));
        currencyPosition = new Vector2();
        stateTime = 0;
        this.animationManager = animationManager;
        initSelfBox();
    }

    public void initSelfBox() {
        box = new Rectangle(0,0,12,7);
        boxOffset = new Vector2(2,0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        animationManager.setReverse(anim_dirt);
        super.setDrawable(animationManager.getKeyFrameDrawable(stateTime));
        super.draw(batch,parentAlpha);
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public void setPosition(Vector2 position) {
        box.setPosition(position.cpy());
        currencyPosition.set(position.sub(boxOffset));
        super.setPosition(position.x, position.y);
    }


    public Vector2 getPosition() {
        return currencyPosition;
    }

    public void setPlayMode(AnimationManager.PlayMode playMode){
        animationManager.setPlayMode(playMode);
    }
}
