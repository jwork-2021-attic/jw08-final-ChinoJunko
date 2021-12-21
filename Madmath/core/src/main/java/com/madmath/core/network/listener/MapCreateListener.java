package com.madmath.core.network.listener;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.network.dto.MapCreateDto;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午9:14
 */
public class MapCreateListener extends AbstractListener<MapCreateDto> {

    MadMath game;

    public MapCreateListener(MadMath game) {
        super(MapCreateDto.class);
        this.game = game;
    }

    @Override
    public void accept(Connection connection, MapCreateDto mapCreateDto) {
        Gdx.app.postRunnable(()->{game.gameScreen.nextMap(GameMap.readMap(mapCreateDto.buffer,game));});
    }
}
