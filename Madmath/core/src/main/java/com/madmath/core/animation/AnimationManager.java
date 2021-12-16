/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:56
*/
package com.madmath.core.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.actor.AnimationActor;

import java.util.Arrays;

public class AnimationManager {
    Array<CustomAnimation> animations;
    PlayMode playMode;
    boolean reverse = false;

    /*
    public static TextureRegion[][] castRegionToArray2(TextureRegion region){
        TextureRegion[][] regions = new TextureRegion[PlayMode.values().length][1];
        for (int i = 0; i < regions.length; i++) {
            regions[i][0] = region;
        }
        return regions;
    }

    public static float[] copyDurationToArray2(float frameDuration){
        float[] Durations = new float[PlayMode.values().length];
        Arrays.fill(Durations, frameDuration);
        return Durations;
    }

     */

    public AnimationManager(TextureRegion region, float frameDuration){
        this(new CustomAnimation(frameDuration,region));
        //this(castRegionToArray2(region),copyDurationToArray2(frameDuration));
    }

    public AnimationManager(TextureRegion[][] regions, float[] frameDurations){
        CustomAnimation[] customAnimations = new CustomAnimation[regions.length];
        for (int i = 0; i < regions.length; i++) {
            customAnimations[i] = new CustomAnimation(frameDurations[i],new Array<>(regions[i]));
        }
        animations = new Array<>();
        for (int i = 0; i < customAnimations.length; i++) {
            animations.add(customAnimations[i]);
        }
        for (int i = 0; i < customAnimations.length; i++) {
            animations.add(customAnimations[i].getReverse());
        }
        playMode = PlayMode.Stand;
    }

    public AnimationManager(CustomAnimation...customAnimations){
        animations = new Array<>();
        for (int i = 0; i < customAnimations.length; i++) {
            animations.add(customAnimations[i]);
        }
        for (int i = 0; i < customAnimations.length; i++) {
            animations.add(customAnimations[i].getReverse());
        }
        playMode = PlayMode.Stand;
    }

    public TextureRegion getKeyFrame(float stateTime){
        return animations.get(reverse? playMode.ordinal() + animations.size/2: playMode.ordinal()).getKeyFrame(stateTime);
    }

    public TextureRegionDrawable getKeyFrameDrawable(float stateTime){
        return animations.get(reverse? playMode.ordinal() + animations.size/2:playMode.ordinal()).getKeyFrameDrawable(stateTime);
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public AnimationManager clone(){
        CustomAnimation[] customAnimations = new CustomAnimation[animations.size];
        System.arraycopy(animations.items,0,customAnimations,0,animations.size);
        return new AnimationManager(customAnimations);
    }

    public enum PlayMode{
        Stand,
        Moving,
        Sprint,
    }
}
