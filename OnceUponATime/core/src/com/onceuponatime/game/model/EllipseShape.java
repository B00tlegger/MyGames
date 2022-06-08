package com.onceuponatime.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class EllipseShape extends PolygonShape {
    private final float hx;
    private final float hy;
    private final Array<Vector2> upHemispherePoints = new Array<>();
    private final Array<Vector2> downHemispherePoints = new Array<>();


    public EllipseShape (float hx, float hy) {
        this.hx = hx;
        this.hy = hy;
        createUpHemisphere();
        createDownHemisphere();
    }

    private void createUpHemisphere () {
        float a = hx / 2;
        float b = hy / 2;
        for(int i = 0; i <= 180; i += 25) {
            double cosX = 0.0;
            double sinY = 0.0;
            if(i != 90) cosX = cos(toRadians(i));
            if(i != 180 && i != 0) sinY = sin(toRadians(i));
            double x = a * cosX;
            double y = b * sinY;
            upHemispherePoints.add(new Vector2((float) (x + a), (float) (y + b)));
        }
    }

    private void createDownHemisphere () {
        float a = hx / 2;
        float b = hy / 2;
        for(int i = 180; i <= 360; i += 25) {
            double cosX = 0.0;
            double sinY = 0.0;
            if(i != 90 && i != 270) cosX = cos(toRadians(i));
            if(i != 180 && i != 0) sinY = sin(toRadians(i));
            double x = a * cosX;
            double y = b * sinY;
            downHemispherePoints.add(new Vector2((float) (x + a), (float) (y + b)));
        }
    }

    public void setUpHemispherePoints () {
        Vector2[] vertexes = new Vector2[upHemispherePoints.size];
        for(int i = 0 ; i < upHemispherePoints.size ; i++){
            vertexes[i] = upHemispherePoints.get(i);
        }
        set(vertexes);
    }

    public void setDownHemispherePoints (){
        Vector2[] vertexes = new Vector2[downHemispherePoints.size];
        for(int i = 0 ; i < downHemispherePoints.size ; i++){
            vertexes[i] = downHemispherePoints.get(i);
        }
        set(vertexes);
    }
}
