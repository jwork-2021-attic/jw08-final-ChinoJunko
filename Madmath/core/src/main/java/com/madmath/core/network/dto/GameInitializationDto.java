package com.madmath.core.network.dto;

import com.badlogic.gdx.utils.Array;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.map.GameMap;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 20/12/2021 下午8:18
 */
public class GameInitializationDto implements Dto{
    public long offset;
    public Player player;
    public int bufferSize;
    public byte[] buffer;
    public Array teammates;
}
