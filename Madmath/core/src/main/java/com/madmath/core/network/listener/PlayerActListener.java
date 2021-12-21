package com.madmath.core.network.listener;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.madmath.core.main.MadMath;
import com.madmath.core.network.dto.PlayerActDto;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午9:26
 */
public class PlayerActListener extends AbstractListener<PlayerActDto> {
    private Server server;
    private Client client;
    private MadMath game;
    boolean isServer;
    public PlayerActListener(Server server,MadMath game) {
        super(PlayerActDto.class);
        this.server = server;
        isServer = true;
        this.game = game;
    }
    public PlayerActListener(Client client,MadMath game) {
        super(PlayerActDto.class);
        this.client = client;
        isServer = false;
        this.game = game;
    }

    @Override
    public void accept(Connection connection, PlayerActDto playerActDto) {
        Gdx.app.postRunnable(()->{
            if(playerActDto.id!=game.gameScreen.player.getId())game.gameScreen.selectPlayer(playerActDto.id).readAct(playerActDto.buffer);
        });
        if(isServer){
            server.sendToAllExceptTCP(connection.getID(),playerActDto);
        }
    }
}
