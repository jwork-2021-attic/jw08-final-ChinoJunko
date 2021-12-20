/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:08
*/
package com.madmath.core.thread;

import com.madmath.core.control.PlayerInputProcessor;
import com.madmath.core.entity.creature.Player;
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
                        if(gameScreen.client!=null)  gameScreen.client.writeAct(gameScreen.player);
                        else if(gameScreen.server!=null)    gameScreen.server.writeAct(gameScreen.player);
                    }
                    gameScreen.player.move(gameScreen.getCurrencyDelta());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
