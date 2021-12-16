package com.madmath.html;

import com.madmath.core.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class mainHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new main();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
