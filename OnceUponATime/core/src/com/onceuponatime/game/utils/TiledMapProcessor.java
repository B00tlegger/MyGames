package com.onceuponatime.game.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TiledMapProcessor {
    private final TiledMap map;
    private final Array<TiledMapTileLayer> layers = new Array<>();
    private final Array<MapLayer> firstRenderLayers = new Array<>();
    private final Queue<MapLayer> queue = new Queue<>();


    public TiledMapProcessor (String path) {
        this.map = new TmxMapLoader().load(path);
        collectLayers();
    }

    public MapLayer getMapLayer (String layerName) {
        return map.getLayers().get(layerName);
    }

    private TiledMapTileLayer.Cell getCell (Vector2 position, TiledMapTileLayer tiledLayer) {
        return tiledLayer.getCell((int) (position.x / tiledLayer.getTileWidth()),
                                  (int) (position.y / tiledLayer.getTileHeight() - 0.5));
    }

    public Iterator<MapObject> getLayerObjectsIterator (String layerName) {
        return map.getLayers().get(layerName).getObjects().iterator();
    }

    public MapObject getMapObject (String layerName, String objectName) {
        return map.getLayers().get(layerName).getObjects().get(objectName);
    }

    public MapObject getMapObject (MapLayer layer, String objectName) {
        return layer.getObjects().get(objectName);
    }

    public Vector2 getObjectPosition (String layerName, String objectName) {
        MapObject object = map.getLayers().get(layerName).getObjects().get(objectName);
        float x = object.getProperties().get("x", Float.class);
        float y = object.getProperties().get("y", Float.class);
        return new Vector2(x, y);
    }

    private void collectLayers () {
        collectFirstStepRenderLayers(map.getLayers());
//        collectTiledMapLayers(map.getLayers());
    }

//    private void collectTiledMapLayers (MapLayers layers) {
//        for(MapLayer mapLayer : layers) {
//            if(mapLayer instanceof MapGroupLayer) {
//                collectTiledMapLayers(((MapGroupLayer) mapLayer).getLayers());
//            } else if(mapLayer.getProperties().get("priority", Integer.class) > 0) {
//                layers.add(mapLayer);
//            }
//        }
//    }

    public boolean hasSomething (TiledMapTileLayer layer, Vector2 position) {
        TiledMapTileLayer.Cell cell = getCell(position, layer);
        return cell != null;
    }

    public void firstStepRender () {
        setVisible(firstRenderLayers, true);
        setVisible(layers, false);
    }

    public void nextStepRender () {
        setVisible(firstRenderLayers, false);
    }

    //TODO: падает с аут оф мемори
    private void collectFirstStepRenderLayers (MapLayers layers) {
        for(MapLayer mapLayer : layers) {
            if(mapLayer instanceof MapGroupLayer) {
                collectFirstStepRenderLayers(((MapGroupLayer) mapLayer).getLayers());
            } else if(mapLayer.getProperties().get("priority", Integer.class) != null) {
                if(mapLayer.getProperties().get("priority", Integer.class) == 0) firstRenderLayers.add(mapLayer);
                if(mapLayer.getProperties().get("priority", Integer.class) > 0) {
                    this.layers.add((TiledMapTileLayer) mapLayer);
                }
            }
        }
    }

    private void setVisible (Array<? extends MapLayer> mapLayers, boolean visible) {
        for(int i = 0; i < mapLayers.size; i++) {
            mapLayers.get(i).setVisible(visible);
        }
    }

    public TiledMap getMap () {
        return map;
    }

    public Queue<MapLayer> getQueue () {
        return queue;
    }

    public Iterator<MapLayer> queueIterator () {
        return queue.iterator();
    }

    public void refillQueue () {
        for(int i = 0; i < layers.size; i++) {
            queue.addLast(layers.get(i));
        }
    }
}
