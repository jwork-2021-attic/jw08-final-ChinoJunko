/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:17
*/
package com.madmath.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.serializer.GameTitle;
import com.madmath.core.serializer.MySerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HUD extends UI{

    Image[] fullHeart;
    Image[] halfHeart;
    Image[] emptyHeart;

    ImageButton menuButton;
    Table slTable;
    Table menuTable;
    Dialog checkDialog;
    TextButton[] textButtons;
    TextButton continuebutton;
    TextButton savebutton;
    TextButton backbutton;
    TextButton backtomenubutton;
    TextButton exitgamebutton;

    Container[] weaponBoxs;
    Equipment[] weapons;

    int maxHeart = 70;


    Label monsterCount;
    Label currentScore;

    Window window;
    Label[] weaponIntro;

    VerticalGroup verticalGroup;

    InputMultiplexer multiplexer;

    Table table;

    Player player;

    public HUD(GameScreen gameScreen, ResourceManager manager){
        super(gameScreen, manager);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);

        table = new Table(manager.skin);
        table.setBackground(new TextureRegionDrawable(manager.flzgbackground700x128));
        table.setPosition(0,0);
        table.setSize(700,128);

        menuTable = new Table(manager.dialogSkin);
        menuTable.setSize(300,300);
        menuTable.setCenterPosition(350,200);
        menuTable.setBackground(new TextureRegionDrawable(manager.tablebackground300x300));


        slTable = new Table(manager.dialogSkin);
        slTable.setSize(300,300);
        slTable.setCenterPosition(350,200);
        slTable.setBackground(new TextureRegionDrawable(manager.tablebackground300x300));
        Table listTable = new Table();
        textButtons = new TextButton[29];
        for (int i = 0; i < 29; i++) {
            textButtons[i] = new TextButton("",manager.skin);
            textButtons[i].getLabel().setFontScale(0.5f);
            try (Input input = new Input(new FileInputStream("./save/save"+(i+1)+".bin"),50<<10);){
                GameTitle gameTitle = MySerializer.defaultKryo.readObject(input,GameTitle.class);
                textButtons[i].setText(("SAVE" + (i + 1) + "\n") + gameTitle.name +" maplvl:"+gameTitle.mapLevel+" dif:"+gameTitle.factor+ "\nscore:" + gameTitle.score);
            } catch (FileNotFoundException e) {
                textButtons[i].setText("Empty");
            }
            int finalI = i+1;
            textButtons[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(!slTable.isVisible()) return;
                    checkDialog = new Dialog("save in this slot?",manager.dialogSkin){
                        @Override
                        protected void result(Object object) {
                            if((boolean)object){
                                saveGame(finalI);
                                slTable.setVisible(false);
                                menuTable.setVisible(true);
                            }
                        }
                    }.button("yes",true).button("no",false).show(stage);
                    //checkDialog.setSize(300,300);
                    //checkDialog.setCenterPosition(350,200);
                }
            });
            listTable.add(textButtons[i]).spaceBottom(10).row();
        }
        ScrollPane scrollPane = new ScrollPane(listTable, manager.skin);
        Label label = new Label("Save", manager.skin);
        label.setFontScale(1.1f);

        continuebutton = new TextButton("Continue", manager.skin);
        continuebutton.getLabel().setFontScale(1.5f);
        savebutton = new TextButton("Save Game", manager.skin);
        savebutton.getLabel().setFontScale(1.5f);
        backtomenubutton = new TextButton("MainMenu", manager.skin);
        backtomenubutton.getLabel().setFontScale(1.5f);
        exitgamebutton = new TextButton("Exit Game", manager.skin);
        exitgamebutton.getLabel().setFontScale(1.5f);
        backbutton = new TextButton("Back", manager.skin);
        backbutton.getLabel().setFontScale(1.5f);
        continuebutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(menuTable.isVisible())   hideMenu();
            }
        });
        savebutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(menuTable.isVisible()){
                    slTable.setVisible(true);
                    menuTable.setVisible(false);
                }
            }
        });
        backbutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(slTable.isVisible()){
                    slTable.setVisible(false);
                    menuTable.setVisible(true);
                }
            }
        });
        backtomenubutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(menuTable.isVisible()){
                    game.gameScreen.resetGame();
                    game.gameScreen.resetMap();
                    gameScreen.switchScreen(game.mainMenuScreen);
                }
            }
        });
        exitgamebutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(menuTable.isVisible()){
                    game.dispose();
                    System.exit(0);
                }
            }
        });
        menuTable.add(new Label("Menu", manager.dialogSkin)).row().spaceBottom(15);
        menuTable.add(continuebutton).row().spaceBottom(15);
        menuTable.add(savebutton).row().spaceBottom(15);
        menuTable.add(backtomenubutton).row().spaceBottom(15);
        menuTable.add(exitgamebutton).row().spaceBottom(15);

        menuButton = new ImageButton(manager.menuButtonStyle);
        menuButton.setSize(100,50);
        menuButton.setPosition(700- menuButton.getWidth(), 400- menuButton.getHeight() );
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(menuButton.isVisible()) showMenu();
            }
        });
        stage.addActor(menuButton);

        slTable.add(label).spaceBottom(15).row();
        slTable.add(new Container<>(scrollPane)).row();
        slTable.add(backbutton).padBottom(10);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        weaponBoxs = new Container[3];
        weapons = new Equipment[3];
        for (int i = 0; i < 3; i++) {
            weaponBoxs[i] =new Container<Actor>(new Image(manager.contain_box_144x144));
            weaponBoxs[i].setBackground(new TextureRegionDrawable(manager.contain_box_144x144));
            horizontalGroup.addActor(weaponBoxs[i]);
            horizontalGroup.space(30);
        }
        Table table1 = new Table();
        table1.add(horizontalGroup).row();
        window = new Window("EmptyHand",manager.dialogSkin);
        weaponIntro = new Label[2];
        weaponIntro[0] = window.add("FIRE PUNCH").getActor();
        weaponIntro[0].setFontScale(0.5f);
        window.row();
        weaponIntro[1] = window.add("").getActor();
        weaponIntro[1].setFontScale(0.5f);
        table1.add(window).expandX();
        table.add(table1).expand();

        fullHeart = new Image[maxHeart];
        halfHeart = new Image[maxHeart];
        emptyHeart = new Image[maxHeart];
        for (int i = 0; i < maxHeart; i++) {
            fullHeart[i] = new Image(manager.ui_heart_full16x16);
            halfHeart[i] = new Image(manager.ui_heart_half16x16);
            emptyHeart[i] = new Image(manager.ui_heart_empty16x16);
            fullHeart[i].setPosition(1f * 16 * i,128);
            halfHeart[i].setPosition(1f * 16 * i,128);
            emptyHeart[i].setPosition(1f * 16 * i,128);
            fullHeart[i].setScale(1f);
            halfHeart[i].setScale(1f);
            emptyHeart[i].setScale(1f);
            stage.addActor(emptyHeart[i]);
            stage.addActor(halfHeart[i]);
            stage.addActor(fullHeart[i]);
        }
        for (int i = 0; i < maxHeart; i++) {
            emptyHeart[i].setZIndex(1);
            halfHeart[i].setZIndex(2);
            fullHeart[i].setZIndex(3);
        }

        stage.addActor(table);
        stage.addActor(menuTable);
        stage.addActor(slTable);

        monsterCount = new Label("",new Label.LabelStyle(manager.font, new Color(1f,0.3f,1f,1f)));
        monsterCount.setFontScale(0.8f);
        currentScore = new Label("",new Label.LabelStyle(manager.font,new Color(1f,0.3f,1f,1f)));
        currentScore.setFontScale(0.8f);
        verticalGroup = new VerticalGroup();
        verticalGroup.addActor(monsterCount);
        verticalGroup.addActor(currentScore);
        table.add(verticalGroup);
        //dialog.setSize();
        //Health = new Label("",new Label.LabelStyle(manager.font, Color.WHITE ));
        //Health.setFontScale(0.5f);
        //Health.setPosition(0,120);
        //stage.addActor(Health);
    }



    public void updateSave(){
        for (int i = 0; i < 29; i++) {
            try (Input input = new Input(new FileInputStream("./save/save" + (i + 1) + ".bin"), 50 << 10);) {
                GameTitle gameTitle = MySerializer.defaultKryo.readObject(input,GameTitle.class);
                textButtons[i].setText(("SAVE" + (i + 1) + "\n") + gameTitle.name +" maplvl:"+gameTitle.mapLevel+" dif:"+gameTitle.factor+ "\nscore:" + gameTitle.score);
            } catch (FileNotFoundException e) {
                textButtons[i].setText("Empty");
            }
        }
    }

    public void showMenu(){
        if(!gameScreen.isOnline)gameScreen.pause();
        menuButton.setVisible(false);
        menuTable.setVisible(true);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void hideMenu(){
        if(!gameScreen.isOnline)gameScreen.resume();
        menuButton.setVisible(true);
        menuTable.setVisible(false);
        slTable.setVisible(false);
        Gdx.input.setInputProcessor(gameScreen.multiplexer);
    }

    public void show(){
        player = gameScreen.player;
        multiplexer.addProcessor(0,player.inputProcessor);
        getStage().addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(1f)));
        menuButton.setVisible(true);
        menuTable.setVisible(false);
        slTable.setVisible(false);
    }

    @Override
    public void update(float dt) {
        //Health.setText(Integer.toString(player.getHp()));
        monsterCount.setText("Monster number: "+(gameScreen.map.livingEntity.size-1));
        currentScore.setText("Current Score: "+(player.score));
        for (int i = 0; i < maxHeart; i++) {
            emptyHeart[i].setVisible((player.getMaxHp() + 1) / 2 > i);
            halfHeart[i].setVisible((player.getHp() + 1) / 2 > i);
            fullHeart[i].setVisible(player.getHp() / 2 > i);
        }
        for (int i = 0; i < player.weapon.size; i++) {
            try {
                if(!(weaponBoxs[i].getActor() instanceof Equipment) || weaponBoxs[i].getActor().getClass().getField("alias").get(null)!=player.weapon.get(i).getClass().getField("alias").get(null)){
                    weapons[i] = player.weapon.get(i).copy();
                    weaponBoxs[i].setActor(weapons[i]);
                    //System.out.println("cc:"+i);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if(i== player.weapon.indexOf(player.activeWeapon,true)){
                window.setTitle(player.activeWeapon.name);
                weaponIntro[0].setText(player.activeWeapon.text[0]);
                weaponIntro[1].setText(player.activeWeapon.text[1]);
                weaponBoxs[i].setColor(player.activeWeapon.color.cpy());
                weaponBoxs[i].getActor().setColor(player.activeWeapon.color.cpy());
            }else {
                weaponBoxs[i].setColor(Color.GRAY.cpy());
                weaponBoxs[i].getActor().setColor(Color.GRAY.cpy());
            }
        }
        //stage.act();
    }

    public void saveGame(int index){
        try (Output output = new Output(new FileOutputStream("./save/"+(index==0?"autosave":"save"+index)+".bin"),50<<10);){
            MySerializer.defaultKryo.writeObject(output,new GameTitle(gameScreen.map.name,gameScreen.map.mapLevel,gameScreen.map.difficultyFactor, player.score));
            MySerializer.defaultKryo.writeObject(output,player);
            MySerializer.defaultKryo.writeObject(output,gameScreen.map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        updateSave();
    }

    public void reset(){
        for (int i = 0; i < 3; i++) {
            if(weaponBoxs[i].getActor()!=null){
                weaponBoxs[i].removeActor(weaponBoxs[i].getActor());
            }
        }
    }

    public String getCurrentScore() {
        return currentScore.getText().toString();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        update(dt);
        stage.act();
        //viewport.update();
        stage.draw();
    }


}
