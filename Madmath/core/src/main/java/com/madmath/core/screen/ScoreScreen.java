package com.madmath.core.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.madmath.core.main.MadMath;
import com.madmath.core.resource.ResourceManager;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 6/12/2021 上午7:19
 */
public class ScoreScreen extends AbstractScreen{

    private Table table;
    private Label label1;
    private Label label2;
    private TextButton againButton;
    private TextButton backButton;

    public ScoreScreen(MadMath game, ResourceManager manager) {
        super(game, manager);
        table = new Table();
        //table.setSize(300,400);
        table.setBackground(new TextureRegionDrawable(manager.background700x400));
        table.setFillParent(true);
        label1 = new Label("Your Score is:",new Label.LabelStyle(manager.font, Color.WHITE));
        label1.setFontScale(0.5f);
        label2 = new Label("0",new Label.LabelStyle(manager.font, Color.YELLOW));
        againButton = new TextButton("Again",manager.dialogSkin);
        againButton.setSize(125,60);
        backButton = new TextButton("Back",manager.dialogSkin);
        backButton.setSize(125,60);
        againButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameScreen.resetGame();
                switchScreen(game.gameScreen);
            }
        });
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreen(game.mainMenuScreen);
            }
        });
        table.add(label1).row().spaceBottom(10);
        table.add(label2).row().spaceBottom(30);
        table.add(againButton).row().spaceBottom(20);
        table.add(backButton).row();
        stage.addActor(table);
    }

    @Override
    public void show() {
        label2.setText(Integer.toString(game.gameScreen.player.score));
        super.show();
    }

    @Override
    public void render(float v) {
        super.render(v);
        stage.act();
        stage.draw();
    }
}
