package com.onceuponatime.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

public class OctagonShape extends PolygonShape {
    private final float hx;
    private final float hy;

    public OctagonShape (float hx, float hy) {
        this.hx = hx;
        this.hy = hy;
        set(createOctagon());
    }

    private Vector2[] createOctagon () {
        final ArrayList<Vector2> points = new ArrayList<>();
        float a = hx / 2;
        float b = hy / 2;
        for(int i = 0; i < 360; i += 45) {
            double cosX = 0.0;
            double sinY = 0.0;
            if(i != 90 && i != 270) cosX = cos(toRadians(i));
            if(i != 180 && i != 0) sinY = sin(toRadians(i));
            double x = a * cosX;
            double y = b * sinY;
            points.add(new Vector2((float) (x + a), (float) (y + b)));
        }
        Vector2[] vertexes = new Vector2[points.size()];
        for(int i = 0 ; i < points.size() ; i++){
            vertexes[i] = points.get(i);
        }
        return vertexes;
    }
}
