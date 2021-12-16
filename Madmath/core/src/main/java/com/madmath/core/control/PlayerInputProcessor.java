/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:56
*/
package com.madmath.core.control;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.madmath.core.entity.creature.Creature;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.screen.AbstractScreen;

public class PlayerInputProcessor extends InputAdapter {
    Player player;

    public PlayerInputProcessor(Player player){
        this.player = player;
        player.inputProcessor = this;
    }

    @Override
    public boolean keyDown(int i) {
        switch (i){
            case Input.Keys.LEFT:
            case Input.Keys.A:
                player.addSubjectiveDirection(new Vector2(-1f,0));
                return true;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                player.addSubjectiveDirection(new Vector2(1f,0));
                return true;
            case Input.Keys.UP:
            case Input.Keys.W:
                player.addSubjectiveDirection(new Vector2(0,1f));
                return true;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                player.addSubjectiveDirection(new Vector2(0,-1f));
                return true;
            case Input.Keys.Q:
            case Input.Keys.TAB:
                player.switchWeapon(+1);
                return true;
            case Input.Keys.E:
                player.pickUp();
                return true;
            case Input.Keys.SHIFT_LEFT:
                player.setSprint(true);
                return true;
            case Input.Keys.PLUS:
            case Input.Keys.Z:
                player.gameScreen.getCamera().zoom = Math.max(player.gameScreen.getCamera().zoom-0.1f,0.5f);
                return true;
            case Input.Keys.MINUS:
            case Input.Keys.X:
                player.gameScreen.getCamera().zoom = Math.min(player.gameScreen.getCamera().zoom+0.1f,1f);
                return true;
            case Input.Keys.NUM_1:
                player.setWeapon(0);
                return true;
            case Input.Keys.NUM_2:
                player.setWeapon(1);
                return true;
            case Input.Keys.NUM_3:
                player.setWeapon(2);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int i) {
        switch (i){
            case Input.Keys.LEFT:
            case Input.Keys.A:
                player.addSubjectiveDirection(new Vector2(1f,0));
                return true;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                player.addSubjectiveDirection(new Vector2(-1f,0));
                return true;
            case Input.Keys.UP:
            case Input.Keys.W:
                player.addSubjectiveDirection(new Vector2(0,-1f));
                return true;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                player.addSubjectiveDirection(new Vector2(0,1f));
                return true;
            case Input.Keys.SHIFT_LEFT:
                player.setSprint(false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        player.gameScreen.getGame().manager.click.play();
        if(player.gameScreen.getState()!= AbstractScreen.State.RUNING) return false;
        if(button == Input.Buttons.LEFT){
            player.swingWeapon();
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        if(player.gameScreen.getState()!= AbstractScreen.State.RUNING) return false;
        Vector3 point = player.gameScreen.getCamera().unproject(new Vector3(i,i1,0));
        player.weaponAngle = new Vector2(point.x-player.getX(),point.y-player.getY()).angle();
        if(player.getState() == Creature.State.Stand){
            player.setAnimDirection(point.x < player.getX());
        }
        return false;
    }


    @Override
    public boolean scrolled(int i) {
        if(i==-1){player.switchWeapon(-1);
        }else{
            player.switchWeapon(+1);
        }
        return false;
    }

}
