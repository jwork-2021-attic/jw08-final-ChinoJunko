/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.entity.creature;

import com.madmath.core.animation.AnimationManager;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class MonsterFactory {

    ResourceManager manager;

    GameScreen gameScreen;

    static private int monstersNextId = 10000;

    public MonsterFactory(ResourceManager manager, GameScreen gameScreen){
        this.manager = manager;
        this.gameScreen = gameScreen;
    }

    @Nullable
    public Monster generateMonsterByName(String name, int id) {
        int temp = monstersNextId;
        monstersNextId = id;
        Monster monster = generateMonsterByName(name);
        monstersNextId = Math.max(monstersNextId,temp);
        return monster;
    }

    @Nullable
    public Monster generateMonsterByName(String name) {
        Iterable<Monster> monsterIter = Monster.monsterSort.select(monster1 -> {
            try {
                return monster1.getClass().getField("alias").get(monster1).equals(name)||monster1.getClass().getName().equals(name);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return false;
        });
        if(monsterIter.iterator().hasNext()){
            Monster monster = monsterIter.iterator().next();
            try {
                   return monster.getClass().getConstructor(Integer.class, AnimationManager.class).newInstance(monstersNextId++,monster.getAnimationManager().clone());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return Monster.monsterSort.get(0).clone(monstersNextId++);
    }
}
