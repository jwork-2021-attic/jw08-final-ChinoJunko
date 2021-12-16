/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.inventory;

import com.badlogic.gdx.math.Vector2;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.animation.AnimationManager;

public class Item extends AnimationActor {
    public Item(AnimationManager animationManager) {
        super(animationManager);
    }

    public boolean canPickUp(Vector2 position) {
        return false;
    }

    public boolean use() {
        return false;
    }

    public Vector2 getItemPosition() {
        return getPosition();
    }
}
