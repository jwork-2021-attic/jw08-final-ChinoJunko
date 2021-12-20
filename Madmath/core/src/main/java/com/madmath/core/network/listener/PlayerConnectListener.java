package com.madmath.core.network.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.main.MadMath;
import com.madmath.core.network.dto.GameInitializationDto;
import com.madmath.core.network.dto.PlayerConnectDto;
import com.madmath.core.network.dto.PlayerCreateDto;
import com.madmath.core.thread.PlayerThread;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午11:42
 */
public class PlayerConnectListener extends AbstractListener<PlayerConnectDto> {

    static int uid = 1;

    private final Server server;

    private final MadMath game;

    public PlayerConnectListener(Server server, MadMath game) {
        super(PlayerConnectDto.class);
        this.server = server;
        this.game = game;
    }

    @Override
    public void accept(Connection connection, PlayerConnectDto playerConnectDto) {
        Gdx.app.postRunnable(()->{
            GameInitializationDto gameInitializationDto = new GameInitializationDto();
            Player player = new Player(uid++,Player.initPlayerAnim(game.manager),game.gameScreen,game.gameScreen.map.getPlayerSpawnPoint().cpy());
            Array<Player> teammates = new Array<>();
            game.gameScreen.teammate.forEach((id,mate)->{teammates.add(mate);});
            teammates.add(game.gameScreen.player);
            gameInitializationDto.player = player;
            Output output = game.gameScreen.map.writeMap();
            gameInitializationDto.bufferSize = output.position();
            gameInitializationDto.buffer = output.getBuffer();
            gameInitializationDto.teammates = teammates;
            server.sendToTCP(connection.getID(), gameInitializationDto);
            PlayerCreateDto playerCreateDto = new PlayerCreateDto();
            playerCreateDto.player = player;
            game.gameScreen.addTeammate(player);
            server.sendToAllExceptTCP(connection.getID(), playerCreateDto);
        });
    }
}

