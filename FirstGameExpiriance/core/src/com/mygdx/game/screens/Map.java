package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.Coordinate;
import com.mygdx.game.model.characters.decorations.Barrel;
import com.mygdx.game.model.characters.decorations.MapDecoration;
import com.mygdx.game.model.characters.decorations.Passability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map {
    private float[][] coordinate;

    public static final int CELLS_X = 16;
    public static final int CELLS_Y = 9;
    public static final int CELL_SIZE = 80;
    HashMap<Vector2, MapDecoration> passability;
    List<Barrel> objectList;

    private Texture grass;
    private Texture barrel;

    public Map () {
        coordinate = new float[CELLS_X][CELLS_Y];
        passability = new HashMap<>();
        objectList = new ArrayList<>();
        for(int i = 0 ; i < 15 ; i++){
            coordinate[MathUtils.random(0, CELLS_X - 1)][MathUtils. random(0, CELLS_Y - 1)] = Passability.UNPASSABLE.getValue();
        }
        grass = new Texture("Grass.png");
        createObjects();
    }

    public void render (SpriteBatch batch) {

        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 9; j++) {
                batch.draw(grass, i * 80, j * 80);
            }
        }
    }
    public void  createObjects(){
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 9; j++) {
                if(coordinate[i][j] == Passability.UNPASSABLE.getValue()) {
                    Barrel woodBarrel = new Barrel(new Vector2(i * CELL_SIZE, j * CELL_SIZE));
                    objectList.add(woodBarrel);
                    fillTheSpace((int) woodBarrel.getPosition().x, (int) woodBarrel.getPosition().y, woodBarrel);
                }
            }
        }
    }

    public boolean isPassable (Vector2 position) {
        if(position.x <= 0 || position.x >= 1280 || position.y >= 720 || position.y <=0){
            return false;
        }

        return passability.get(new Vector2(Math.round(position.x), Math.round(position.y))) == null;
    }

    private void fillTheSpace (int x, int y, MapDecoration mapDecoration) {
        for(int i = x; i < x + mapDecoration.getWidth(); i++) {
            for(int j = y; j < y + mapDecoration.getDepth(); j++) {
                passability.put(new Vector2(i, j), mapDecoration);
            }
        }
    }

    public List<Barrel> getObjectList () {
        return objectList;
    }
}
