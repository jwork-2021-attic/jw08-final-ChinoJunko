package com.madmath.core.network.listener;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.madmath.core.main.MadMath;
import com.madmath.core.network.dto.PlayerCreateDto;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午10:37
 */
public class PlayerCreateListener extends AbstractListener<PlayerCreateDto> {

    MadMath game;

    public PlayerCreateListener(MadMath game){
        super(PlayerCreateDto.class);
        this.game = game;
    }

    @Override
    public void accept(Connection conncetion, PlayerCreateDto elem) {
        Gdx.app.postRunnable(()->{
            if(game.gameScreen.selectPlayer(elem.player.getId())==null) game.gameScreen.addTeammate(elem.player);
        });
    }
}
