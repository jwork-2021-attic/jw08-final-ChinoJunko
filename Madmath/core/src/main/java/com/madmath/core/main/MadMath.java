/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.entity.creature.Monster;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.entity.obstacle.Obstacle;
import com.madmath.core.network.Task;
import com.madmath.core.save.Save;
import com.madmath.core.screen.ScoreScreen;
import com.madmath.core.screen.SelectScreen;
import com.madmath.core.util.Utils;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.screen.MainMenuScreen;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MadMath extends Game {

	public static final String VERSION = "0.9";
	public static final String TITLE = "MadMath v"+ VERSION;

	//Screen dimensions
	public static final int V_WIDTH = 700;
	public static final int V_HEIGHT = 400;
	public static final int V_SCALE = 1;

	public SpriteBatch batch;

	public ResourceManager manager;

	public Label fps;

	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;
	public ScoreScreen scoreScreen;
	public SelectScreen selectScreen;

	public Save save;

	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new ResourceManager();

		Task.taskExc = new Task(this);

		Utils.initUtils(manager);

		fps = new Label("", new Label.LabelStyle(manager.font, Color.YELLOW ));
		fps.setFontScale(0.5f);
		fps.setVisible(true);


		Monster.loadMonsters();
		Equipment.loadEquipment();
		Obstacle.loadObstacle();

		save = new Save(this);

		mainMenuScreen = new MainMenuScreen(this, manager);
		gameScreen = new GameScreen(this,manager);
		scoreScreen = new ScoreScreen(this,manager);
		selectScreen = new SelectScreen(this,manager);

		setScreen(mainMenuScreen);
	}

	@Override
	public void resize (int width, int height) {
		getScreen().resize(width, height);
	}

	@Override
	public void render () {
		Task.taskExc.runTask();
		fps.setText(Gdx.graphics.getFramesPerSecond() + " fps");
		super.render();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		//mainMenuScreen.dispose();
		//gameScreen.dispose();
		//scoreScreen.dispose();
		//selectScreen.dispose();
	}
}
