package com.madmath.core.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.madmath.core.main.MadMath;
import com.madmath.core.network.Client;
import com.madmath.core.network.Server;
import com.madmath.core.network.Task;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.util.Utils;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 6/12/2021 上午7:19
 */
public class SelectScreen extends AbstractScreen{

    private final Table olTable;
    private final Table difTable;
    private final Label label1;
    private final TextButton[] gameModeButtons;

    public SelectScreen(MadMath game, ResourceManager manager) {
        super(game, manager);
        Table table = new Table();
        //table.setSize(300,400);
        table.setBackground(new TextureRegionDrawable(manager.background700x400));
        table.setFillParent(true);

        label1 = new Label("Choose the Game Mode:",new Label.LabelStyle(manager.font, Color.WHITE));
        label1.setFontScale(0.5f);
        table.add(label1).spaceBottom(30).row();
        gameModeButtons = new TextButton[2];
        gameModeButtons[0] = new TextButton("Single player mode", manager.dialogSkin);
        gameModeButtons[0].setSize(200, 75);
        gameModeButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectDifShow();
            }
        });
        table.add(gameModeButtons[0]).spaceBottom(20).row();
        gameModeButtons[1] = new TextButton("Multiplayer Mode", manager.dialogSkin);
        gameModeButtons[1].setSize(200, 75);
        gameModeButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectGameModeShow();
            }
        });
        table.add(gameModeButtons[1]).spaceBottom(20).row();
        stage.addActor(table);

        difTable = new Table();
        difTable.setSize(700,400);
        Label label2 = new Label("Choose the Difficulty:", new Label.LabelStyle(manager.font, Color.WHITE));
        label2.setFontScale(0.5f);
        difTable.add(label2).spaceBottom(30).row();
        TextButton[] difButtons = new TextButton[4];
        for (int i = 0; i < 4; i++) {
            difButtons[i] = new TextButton(Utils.DifficultyName[i], manager.dialogSkin);
            difButtons[i].setSize(200, 75);
            int finalI = i;
            difButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(game.gameScreen.isOnline)    game.gameScreen.startGameOnline();
                    game.gameScreen.changeDifficulty((float) (1+0.5* finalI));
                    switchScreen(game.gameScreen);
                }
            });
            difTable.add(difButtons[i]).spaceBottom(20).row();
        }
        stage.addActor(difTable);

        olTable = new Table();
        olTable.setSize(700,400);
        Label label3 = new Label("Online Game:", new Label.LabelStyle(manager.font, Color.WHITE));
        label3.setFontScale(0.5f);
        olTable.add(label3).spaceBottom(30).row();
        TextButton[] olButtons = new TextButton[2];
        olButtons[0] = new TextButton("Create New Online Game", manager.dialogSkin);
        olButtons[0].setSize(200, 75);
        olButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameScreen.server = new Server(game);
                game.gameScreen.isOnline = true;
                selectDifShow();
            }
        });
        olTable.add(olButtons[0]).spaceBottom(20).row();
        olButtons[1] = new TextButton("Connect To Join", manager.dialogSkin);
        olButtons[1].setSize(200, 75);
        olButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameScreen.client = new Client(game);
                game.gameScreen.isOnline = true;
                game.gameScreen.startGameOnline();
            }
        });
        olTable.add(olButtons[1]).spaceBottom(20).row();
        stage.addActor(olTable);
    }

    void selectGameModeShow(){
        difTable.setVisible(false);
        label1.setVisible(false);
        for (int i = 0; i < gameModeButtons.length; i++) {
            gameModeButtons[i].setVisible(false);
        }
        olTable.setVisible(true);
    }

    void selectDifShow(){
        olTable.setVisible(false);
        label1.setVisible(false);
        for (int i = 0; i < gameModeButtons.length; i++) {
            gameModeButtons[i].setVisible(false);
        }
        difTable.setVisible(true);
    }

    void resetButton(){
        label1.setVisible(true);
        for (int i = 0; i < gameModeButtons.length; i++) {
            gameModeButtons[i].setVisible(true);
        }
        difTable.setVisible(false);
        olTable.setVisible(false);
    }

    @Override
    public void show() {
        resetButton();
        super.show();
    }

    @Override
    public void render(float v) {
        super.render(v);
        stage.act();
        stage.draw();
        if(game.gameScreen.isOnline&&game.gameScreen.client!=null&&game.gameScreen.client.finishInit)switchScreen(game.gameScreen);
    }
}
