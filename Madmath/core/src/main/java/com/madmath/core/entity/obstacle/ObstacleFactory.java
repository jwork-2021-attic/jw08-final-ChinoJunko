package com.madmath.core.entity.obstacle;

import com.madmath.core.animation.AnimationManager;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:24
 */
public class ObstacleFactory {
    ResourceManager manager;

    GameScreen gameScreen;

    //static private int obstacleNextId = 3500;

    public ObstacleFactory(ResourceManager manager, GameScreen gameScreen){
        this.manager = manager;
        this.gameScreen = gameScreen;
    }

    @Nullable
    public Obstacle generateObstacleByName(String name) {
        Iterable<Obstacle> obstacleIter = Obstacle.obstacleSort.select(obstacle -> {
            try {
                return obstacle.getClass().getField("alias").get(obstacle).equals(name)
                        || obstacle.getClass().getName().equals(name)
                        || obstacle.getClass().getName().substring(obstacle.getClass().getName().lastIndexOf(".")+1).equals(name);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return false;
        });
        if(obstacleIter.iterator().hasNext()){
            Obstacle obstacle = obstacleIter.iterator().next();
            try {
                return obstacle.getClass().getConstructor(AnimationManager.class).newInstance(obstacle.getAnimationManager().clone());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return Obstacle.obstacleSort.get(0).copy();
    }
}
