package com.madmath.core.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.madmath.core.main.MadMath;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.util.Utils;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 6/12/2021 上午7:19
 */
public class SelectScreen extends AbstractScreen{

    private Table table;
    private Label label1;
    private TextButton[] buttons;

    public SelectScreen(MadMath game, ResourceManager manager) {
        super(game, manager);
        table = new Table();
        //table.setSize(300,400);
        table.setBackground(new TextureRegionDrawable(manager.background700x400));
        table.setFillParent(true);
        label1 = new Label("Choose the Difficulty:",new Label.LabelStyle(manager.font, Color.WHITE));
        label1.setFontScale(0.5f);
        table.add(label1).spaceBottom(30).row();
        buttons = new TextButton[4];
        for (int i = 0; i < 4; i++) {
            buttons[i] = new TextButton(Utils.DifficultyName[i], manager.dialogSkin);
            buttons[i].setSize(200, 75);
            int finalI = i;
            buttons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.gameScreen.changeDifficulty((float) (1+0.5* finalI));
                    switchScreen(game.gameScreen);
                }
            });
            table.add(buttons[i]).spaceBottom(20).row();
        }
        stage.addActor(table);
    }

    @Override
    public void render(float v) {
        super.render(v);
        stage.act();
        stage.draw();
    }
}
