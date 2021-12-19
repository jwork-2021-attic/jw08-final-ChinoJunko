/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:24
*/
package com.madmath.core.thread;

import com.badlogic.gdx.utils.Array;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.screen.AbstractScreen;
import com.madmath.core.screen.GameScreen;

import java.util.HashMap;
import java.util.Map;

public class MonsterThread implements Runnable {

    GameScreen gameScreen;

    public Map<Integer,Monster> monsters;

    private Array monsterToDie;

    public MonsterThread(){
        monsters = new HashMap<>();
        monsterToDie = new Array();
    }

    @Override
    public void run() {
        gameScreen = GameScreen.getCurrencyGameScreen();

        while (true || gameScreen.getState()!= AbstractScreen.State.END){
            try {
                gameScreen.monsterSemaphore.acquire();
                //act monster
                while(monsterToDie.size>0){
                    Monster monster = (Monster) monsterToDie.pop();
                    monsters.remove(monster.getId(),monster);
                }
                monsters.values().forEach(monster -> {
                    monster.monsterAct(gameScreen.getCurrencyDelta());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void addMonster(Monster monster){
        monsters.put(monster.getId(),monster);
    }

    public void removeMonster(Monster monster){
        monsterToDie.add(monster);
    }

    public void reset(){
        monsters.clear();
        gameScreen = GameScreen.getCurrencyGameScreen();
    }
}
