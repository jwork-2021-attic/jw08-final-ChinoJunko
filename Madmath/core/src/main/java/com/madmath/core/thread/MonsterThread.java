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

public class MonsterThread implements Runnable {

    GameScreen gameScreen;

    public Array<Monster> monsters;

    public MonsterThread(){
        monsters = new Array<>();
    }

    @Override
    public void run() {
        gameScreen = GameScreen.getCurrencyGameScreen();

        while (true || gameScreen.getState()!= AbstractScreen.State.END){
            try {
                gameScreen.monsterSemaphore.acquire();
                //act monster
                monsters.forEach(monster -> {
                    monster.monsterAct(gameScreen.getCurrencyDelta());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public void addMonster(Monster monster){
        monsters.add(monster);
    }

    public boolean removeMonster(Monster monster){
        return monsters.removeValue(monster,true);
    }

    public void reset(){
        monsters.clear();
        gameScreen = GameScreen.getCurrencyGameScreen();
    }
}
