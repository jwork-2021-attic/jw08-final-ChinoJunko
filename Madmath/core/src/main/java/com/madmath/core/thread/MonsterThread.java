/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:24
*/
package com.madmath.core.thread;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.entity.creature.Imp;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.network.dto.MonsterActDto;
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
                MonsterActDto monsterActDto = new MonsterActDto();
                Output output = new Output(new byte[10<<10]);
                monsters.values().forEach(monster -> {
                    monster.monsterAct(gameScreen.getCurrencyDelta());
                    if(gameScreen.myServer!=null)   {
                        output.writeInt(monster.getId());
                        monster.writeAct(output);
                    }
                });
                if(gameScreen.myServer!=null){
                    monsterActDto.bufferSize = output.position();
                    monsterActDto.buffer = output.getBuffer();
                    gameScreen.myServer.sendToAllTCP(monsterActDto);
                }
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
