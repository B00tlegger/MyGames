package com.onceuponatime.game.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.onceuponatime.game.OnceUponATime;
import com.onceuponatime.game.model.characters.MapObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RenderHelper {

    public static void render (TiledMapProcessor map,
                               List<MapObject> renderQueue,
                               OnceUponATime game,
                               BitmapFont font24,
                               OrthogonalTiledMapRenderer renderer) {
        List<MapObject> renderedObjects = new ArrayList<>();
        Iterator<MapLayer> iterator = map.queueIterator();
        long start = System.currentTimeMillis();
        while(iterator.hasNext()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) iterator.next();
            layer.setVisible(true);
            for(int i = 0; i < renderQueue.size(); i++) {
                MapObject mapObject = renderQueue.get(i);
                if(map.hasSomething(layer, mapObject.getPosition())) {
                    mapObject.render(game.getBatch(), font24);
                    renderedObjects.add(mapObject);
                }
            }
            renderQueue.removeAll(renderedObjects);
            renderer.render();
            layer.setVisible(false);
        }
        for(int i = 0; i < renderQueue.size(); i++) {
            renderQueue.get(i).render(game.getBatch(), font24);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
