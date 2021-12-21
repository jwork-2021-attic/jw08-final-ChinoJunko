package com.madmath.core.network.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.madmath.core.entity.creature.Imp;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.main.MadMath;
import com.madmath.core.network.dto.MapCreateDto;
import com.madmath.core.network.dto.MonsterActDto;
import com.madmath.core.network.dto.PlayerActDto;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午9:26
 */
public class MonsterActListener extends AbstractListener<MonsterActDto> {
    private Client client;
    private MadMath game;

    public MonsterActListener(Client client, MadMath game) {
        super(MonsterActDto.class);
        this.client = client;
        this.game = game;
    }

    @Override
    public void accept(Connection connection, MonsterActDto monsterActDto) {
        Gdx.app.postRunnable(()->{
            Input input = new Input(monsterActDto.buffer);
            while(input.position()< monsterActDto.bufferSize){
                int mid = input.readInt();
                Monster monster = game.gameScreen.monsterManager.monsters.get(mid);
                if(monster==null)   return;
                else {
                    monster.readAct(input);
                }
            }
        });
    }
}
