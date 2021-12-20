/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:08
*/
package com.madmath.core.thread;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.network.dto.PlayerActDto;
import com.madmath.core.screen.AbstractScreen;
import com.madmath.core.screen.GameScreen;

public class PlayerThread implements Runnable {

    GameScreen gameScreen;

    @Override
    public void run() {
        gameScreen = GameScreen.getCurrencyGameScreen();
        while (true || gameScreen.getState()!= AbstractScreen.State.END){
            try {
                gameScreen.playerSemaphore.acquire();
                if(gameScreen.player!=null){
                    if(gameScreen.isOnline){
                        if(gameScreen.myClient !=null)  {
                            Gdx.app.postRunnable(()->{
                                gameScreen.myClient.sendTCP(writeAct(gameScreen.player));
                            });
                        }
                        else if(gameScreen.myServer !=null)  {
                            Gdx.app.postRunnable(()->{
                                gameScreen.myServer.sendToAllTCP(writeAct(gameScreen.player));
                            });
                        }
                    }
                    gameScreen.player.move(gameScreen.getCurrencyDelta());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private PlayerActDto writeAct(Player player){
        PlayerActDto playerActDto = new PlayerActDto();
        playerActDto.id = player.getId();
        playerActDto.buffer = new byte[1024];
        Output output = new Output(playerActDto.buffer);
        player.writeAct(output);
        playerActDto.bufferSize = output.position();
        return playerActDto;
    }

}
