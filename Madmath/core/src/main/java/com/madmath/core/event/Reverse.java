package com.madmath.core.event;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.madmath.core.screen.GameScreen;


/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 21/12/2021 上午11:54
 */
public class Reverse {
    GameScreen gameScreen;
    float timeout = 50;
    public Reverse(){
        gameScreen = GameScreen.getCurrencyGameScreen();
    }
    public void start(){
        Label label = new Label("REVERSE!",gameScreen.getManager().dialogSkin);
        gameScreen.hud.getStage().addActor(label);
        label.addAction(Actions.sequence(Actions.delay(timeout),Actions.run(()->{gameScreen.hud.getStage().getActors().removeValue(label,true);})));

    }
}
