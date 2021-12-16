/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 6/12/2021 下午4:48
*/
package com.madmath.core.entity.obstacle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.entity.Entity;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class Obstacle extends Entity {

    public static Array<Obstacle> obstacleSort;

    public Obstacle(AnimationManager animationManager) {
        super(animationManager.clone());
    }

    @Override
    public void initSelf(){
        super.initSelf();
        setUserObject(30);
    }

    public Obstacle copy(){
        return GameScreen.getCurrencyGameScreen().getObstacleFactory().generateObstacleByName(getClass().getName());
    }

    static public void loadObstacle(){
        obstacleSort = new Array<>();
        for (String name: Utils.AllDefaultObstacleSort) {
            try {
                Class<?> c = Class.forName("com.madmath.core.entity.obstacle."+name);
                Constructor<?> con = c.getConstructor(AnimationManager.class);
                obstacleSort.add((Obstacle) con.newInstance(new AnimationManager(ResourceManager.defaultManager.LoadObstacleAssetsByName((String) c.getField("alias").get(null),(int)c.getField("oWidth").get(null),(int)c.getField("oHeight").get(null)), new float[]{(float) c.getField("frameInterval").get(null)})));
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Obstacle Named '" +name+'\'');
            } catch (NoSuchFieldException e){
                System.out.println("Not Such Field :'" +e.getMessage()+'\'');
            } catch (IllegalAccessException e){
                System.out.println("IllegalAccessField :'" +e.getMessage()+'\'');
            } catch (NoSuchMethodException e){
                System.out.println("Not Such Method :'" +e.getMessage()+'\'');
                e.printStackTrace();
            }
            catch (InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
    }

}
