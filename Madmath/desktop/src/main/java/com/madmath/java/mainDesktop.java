package com.madmath.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.madmath.core.main.MadMath;

public class mainDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MadMath.V_WIDTH * MadMath.V_SCALE;
		config.height = MadMath.V_HEIGHT * MadMath.V_SCALE;
		config.title = MadMath.TITLE;
		config.resizable = true;
		config.vSyncEnabled = false;
		config.backgroundFPS = 10;
		config.foregroundFPS = 10;
		new LwjglApplication(new MadMath(), config);
	}
}
