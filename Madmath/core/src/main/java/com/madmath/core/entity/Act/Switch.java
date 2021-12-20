package com.madmath.core.entity.Act;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.entity.creature.Player;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 上午5:40
 */
public class Switch implements Act{
    @Override
    public void write(Output output, Player player) {
        output.writeInt(player.weapon.indexOf(player.activeWeapon, true));
    }

    @Override
    public void read(Input input, Player player) {
        player.setWeapon(input.readInt());
    }
}
