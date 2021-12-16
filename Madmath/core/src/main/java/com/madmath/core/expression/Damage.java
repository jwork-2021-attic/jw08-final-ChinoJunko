package com.madmath.core.expression;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.madmath.core.entity.creature.Monster;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 6/12/2021 下午9:13
 */
public class Damage extends Label {

    public Damage(Monster monster, int damage){
        this(Integer.toString(damage),new LabelStyle(monster.gameScreen.getManager().font, Color.YELLOW));
        monster.getStage().addActor(this);
        //setZIndex(5000);
        setFontScale((damage+150)/450f);
        addAction(Actions.sequence(Actions.moveTo(monster.getCenterX(),monster.getY()+monster.getHeight()),
                Actions.addAction(Actions.moveBy(0,32,1f)),
                Actions.addAction(Actions.fadeOut(1f)),
                Actions.delay(1f),
                Actions.run(()->{monster.getStage().getActors().removeValue(this,true);})));
    }

    public Damage(CharSequence text, LabelStyle labelStyle) {
        super(text, labelStyle);
    }
}
