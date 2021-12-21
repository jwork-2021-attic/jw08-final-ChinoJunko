package com.madmath.core.network.listener;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.AnimTile;
import com.madmath.core.map.GameMap;
import com.madmath.core.network.dto.GameInitializationDto;
import com.madmath.core.screen.AbstractScreen;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午7:18
 */
public class GameInitializerListener extends AbstractListener<GameInitializationDto> {

    private final MadMath game;

    private final Client client;

    public GameInitializerListener(MadMath game, Client kryoClient) {
        super(GameInitializationDto.class);
        this.game = game;
        this.client = kryoClient;
    }

    @Override
    public void accept(Connection connection, GameInitializationDto gameInitializationDto) {
        AnimTile.syncTime(gameInitializationDto.offset);
        /* Draw the screen to start the game. */
        Gdx.app.postRunnable(() -> {
            game.gameScreen.addPlayer(gameInitializationDto.player);
            GameMap.readMap(gameInitializationDto.buffer,game);
            game.gameScreen.putPlayers();
            gameInitializationDto.teammates.forEach(teammate->{
                game.gameScreen.addTeammate((Player) teammate);
            });
            game.gameScreen.setState(AbstractScreen.State.PAUSE);
            game.selectScreen.switchScreen(game.gameScreen);

            client.addListener(new PlayerActListener(client,game));
            client.addListener(new MapCreateListener(game));
            client.addListener(new PlayerCreateListener(game));
            client.addListener(new MonsterActListener(client,game));
        });
    }
}

