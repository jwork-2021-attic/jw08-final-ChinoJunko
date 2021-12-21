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

    public Damage(Monster monster, Expression expression){
        this((expression.getResult()>0?"-":"+") + Math.abs(expression.getResult()), new LabelStyle(monster.gameScreen.getManager().font, expression.getColor()));
        monster.getStage().addActor(this);
        setFontScale( (float) Math.log10(2+(Math.abs(expression.getResult())/70f)));
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
