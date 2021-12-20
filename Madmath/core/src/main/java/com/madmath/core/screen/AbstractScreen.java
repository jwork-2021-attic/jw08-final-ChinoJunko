/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:00
*/
package com.madmath.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.madmath.core.main.MadMath;
import com.madmath.core.resource.ResourceManager;

import java.util.concurrent.Semaphore;

public abstract class AbstractScreen implements Screen {

    protected final MadMath game;
    protected final ResourceManager manager;

    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Stage stage;

    //public Semaphore net = new Semaphore(0);

    protected State state;

    protected Music music;

    public enum State{
        READY,
        RUNING,
        PAUSE,
        END,
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public AbstractScreen(final MadMath game, final ResourceManager manager){
        this.game = game;
        this.manager = manager;

        state = State.READY;

        camera = new OrthographicCamera(MadMath.V_WIDTH, MadMath.V_HEIGHT);
        camera.setToOrtho(false);

        viewport = new StretchViewport(MadMath.V_WIDTH, MadMath.V_HEIGHT,camera);

        stage = new Stage(viewport, game.batch);

        music = manager.creditMusic;
    }

    @Override
    public void render(float v) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void update(float v) {}

    public void switchScreen(Screen screen){
        state = State.PAUSE;
        music.stop();
        game.setScreen(screen);
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1);
    }

    @Override
    public void show() {
        state = State.RUNING;
        music.play();
        music.setVolume(1f);
        music.setLooping(true);
        game.fps.setPosition(MadMath.V_WIDTH-55, MadMath.V_HEIGHT-7);
        stage.addActor(game.fps);
        game.fps.setZIndex(1000);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
        state = State.PAUSE;
    }

    @Override
    public void resume() {
        state = State.RUNING;
    }

    public ResourceManager getManager() {
        return manager;
    }

    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public void dispose() {
        stage.dispose();
        state = State.END;
    }

    public Stage getStage() {
        return stage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public MadMath getGame() {
        return game;
    }
}
