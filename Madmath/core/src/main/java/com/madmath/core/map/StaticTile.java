/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class StaticTile extends StaticTiledMapTile {
    public StaticTile(TextureRegion textureRegion) {
        super(textureRegion);
    }

    public StaticTile(StaticTile staticTile){
        super(staticTile);
    }

    public enum TileSort{
        empty_tile,floor_1,floor_2,floor_3,floor_4,floor_5,floor_6,floor_7,floor_8,
        floor_ladder,hole,floor_stair,
        wall_left,wall_mid,wall_right,wall_hole_1,wall_hole_2,
        wall_banner_blue,wall_banner_green,wall_banner_red,wall_banner_yellow
    }
}
