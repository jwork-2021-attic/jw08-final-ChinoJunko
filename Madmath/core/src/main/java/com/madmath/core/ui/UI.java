/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:12
*/
package com.madmath.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.madmath.core.main.MadMath;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

/**
 * Superclass for all UI
 * Contains useful variables and references
 *
 * @author Ming Li
 */
public abstract class UI implements Disposable {

    protected Stage stage;
    protected Viewport viewport;

    protected ResourceManager manager;
    protected GameScreen gameScreen;
    protected MadMath game;

    // graphics
    protected ShapeRenderer shapeRenderer;

    public UI(final MadMath game, ResourceManager manager) {
        this.game = game;
        this.manager = manager;

        viewport = new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        shapeRenderer = new ShapeRenderer();
    }

    public UI(GameScreen gameScreen, ResourceManager manager) {
        this.game = gameScreen.getGame();
        this.gameScreen = gameScreen;
        this.manager = manager;

        viewport = new StretchViewport(700,400);
        stage = new Stage(viewport, gameScreen.getGame().batch);

        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(float dt);

    public void resize(int i, int i1){
        stage.getViewport().update(i, i1);
    }

    public void render(float dt){
    };

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }

}