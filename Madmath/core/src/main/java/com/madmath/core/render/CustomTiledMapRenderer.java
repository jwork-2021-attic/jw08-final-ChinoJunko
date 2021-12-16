/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:58
*/
package com.madmath.core.render;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class CustomTiledMapRenderer extends OrthogonalTiledMapRenderer {

    public CustomTiledMapRenderer(TiledMap map) {
        super(map);
    }

    @Override
    public void renderObject(MapObject object) {

    }

}
