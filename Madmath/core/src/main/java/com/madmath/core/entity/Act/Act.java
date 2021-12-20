package com.madmath.core.entity.Act;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.madmath.core.entity.creature.Player;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 上午5:39
 */
public interface Act{
    public void write(Output output,Player player);

    public void read(Input input,Player player);
}
