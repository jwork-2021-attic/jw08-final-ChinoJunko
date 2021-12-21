/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:07
*/
package com.madmath.core.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryo.io.Input;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.serializer.GameTitle;
import com.madmath.core.serializer.MySerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class MainMenuScreen extends AbstractScreen {

    private Table slTable;
    private final Image gametitle;
    private final ImageButton[] buttons;
    private final ImageButton cancel;
    private final int buttons_num;
    private TextButton[] textButtons;

    public MainMenuScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);

        camera.zoom = 0.5f;

        Table table = new Table();
        table.setBackground(new TextureRegionDrawable(manager.scorebackground));
        table.setFillParent(true);
        stage.addActor(table);

        gametitle = new Image(manager.gametitle200x100);
        gametitle.setPosition(240,270);
        stage.addActor(gametitle);
        gametitle.setZIndex(1);

        buttons_num = 3;
        buttons = new ImageButton[buttons_num];
        buttons[0] = new ImageButton(manager.startButtonStyle);
        buttons[1] = new ImageButton(manager.loadButtonStyle);
        buttons[2] = new ImageButton(manager.exitButtonStyle);
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].setSize(100,50);
            stage.addActor(buttons[i]);
        }

        slTable = new Table(manager.dialogSkin);
        slTable.setSize(300,300);
        slTable.setCenterPosition(350,200);
        slTable.setBackground(new TextureRegionDrawable(manager.tablebackground300x300));
        stage.addActor(slTable);
        Table listTable = new Table();
        textButtons = new TextButton[30];
        try {
            Files.createDirectories(Paths.get("./save"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 30; i++) {
            textButtons[i] = new TextButton("",manager.skin);
            textButtons[i].getLabel().setFontScale(0.5f);
            try (Input input = new Input(new FileInputStream(i==0?"./save/autosave.bin":"./save/save"+i+".bin"),50<<10);){
                GameTitle gameTitle = MySerializer.defaultKryo.readObject(input,GameTitle.class);
                textButtons[i].setText(("SAVE" + i  + "\n") + gameTitle.name +" maplvl:"+gameTitle.mapLevel+" dif:"+gameTitle.factor+ "\nscore:" + gameTitle.score);
                int finalI = i;
                textButtons[i].addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if(!slTable.isVisible()) return;
                        loadGame(finalI);
                    }
                });
            } catch (FileNotFoundException e) {
                textButtons[i].setText("Empty");
            }
            listTable.add(textButtons[i]).spaceBottom(10).row();
        }
        ScrollPane scrollPane = new ScrollPane(listTable, manager.skin);
        //scrollPane.
        scrollPane.setVariableSizeKnobs(true);
        Label label = new Label("Load", manager.skin);
        label.setFontScale(1.3f);
        slTable.add(label).spaceBottom(20).row();
        slTable.add(scrollPane).padBottom(10);
        slTable.setVisible(false);
        cancel = new ImageButton(manager.cancelButtonStyle);
        cancel.setSize(50,50);
        cancel.setCenterPosition(500,350);
        stage.addActor(cancel);
        music = manager.titleMusic;
        buttons[0].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(slTable.isVisible()) return;
                startGame();
            }
        });
        buttons[1].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(slTable.isVisible()) return;
                cancel.setVisible(true);
                slTable.setVisible(true);
                //loadGame(0);
            }
        });
        buttons[2].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(slTable.isVisible()) return;
                game.dispose();
                System.exit(0);
            }
        });
        cancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!slTable.isVisible()) return;
                //cancel.setChecked(false);
                cancel.setVisible(false);
                slTable.setVisible(false);
            }
        });
    }

    @Override
    public void show() {
        super.show();
        resetTitle();
    }

    public void resetTitle(){
        updateSave();
        slTable.setVisible(false);
        cancel.setVisible(false);
        gametitle.setVisible(true);
        gametitle.addAction(Actions.sequence(Actions.alpha(0f),Actions.delay(0.1f),Actions.alpha(1f,2f)));
        for (int i = 0; i < buttons_num; i++) {
            //buttons[i].setChecked(false);
            buttons[i].addAction(Actions.sequence(Actions.delay(0.1f),
                    Actions.moveTo(300,-100),Actions.show(),Actions.moveTo(300,185-65*i,1.1f),
                    Actions.addAction(Actions.forever(Actions.sequence(
                            Actions.moveBy(0,5,1f),
                            Actions.moveBy(0,-10,2f),
                            Actions.moveBy(0,5,1f))))));
        }
    }

    public void startGame(){
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].clearActions();
            buttons[i].setVisible(false);
        }
        gametitle.setVisible(false);
        game.gameScreen.state=State.READY;
        switchScreen(getGame().selectScreen);
    }

    public void loadGame(int index){
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].clearActions();
            buttons[i].setVisible(false);
        }
        gametitle.setVisible(false);
        try (Input input = new Input(new FileInputStream(index==0?"./save/autosave.bin":"./save/save"+index+".bin"),50<<10);){
            MySerializer.defaultKryo.readObject(input,GameTitle.class);
            Player player = game.gameScreen.addPlayer(MySerializer.defaultKryo.readObject(input,Player.class));
            Vector2 position = player.getPosition();
            MySerializer.defaultKryo.readObject(input, GameMap.class);
            game.gameScreen.updateMapTitle();
            player.setPosition(position);
            game.gameScreen.state = State.PAUSE;
        } catch (FileNotFoundException e) {
            System.out.println("Cannot load save!");
            e.printStackTrace();
        }
        switchScreen(game.gameScreen);
    }

    public void updateSave(){
        for (int i = 0; i < 30; i++) {
            try (Input input = new Input(new FileInputStream(i==0?"./save/autosave.bin":"./save/save"+i+".bin"),50<<10);){
                if(textButtons[i].getText().toString().equals("Empty")){
                    int finalI = i;
                    textButtons[i].addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if(!slTable.isVisible()) return;
                            loadGame(finalI);
                        }
                    });
                }
                GameTitle gameTitle = MySerializer.defaultKryo.readObject(input,GameTitle.class);
                textButtons[i].setText(((i==0?"ATUOSAVE":"SAVE" + i) + "\n") + gameTitle.name +" maplvl:"+gameTitle.mapLevel+" dif:"+gameTitle.factor+ "\nscore:" + gameTitle.score);
            } catch (FileNotFoundException e) {
                textButtons[i].setText("Empty");
            }
        }
    }

    @Override
    public void update(float v) {
    }

    @Override
    public void render(float v) {
        update(v);
        super.render(v);
        if(camera.zoom<1f)   camera.zoom+=0.005f;
        camera.update();
        stage.act(v);
        stage.draw();
    }

}
