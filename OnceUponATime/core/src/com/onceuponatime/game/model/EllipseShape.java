package com.onceuponatime.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class EllipseShape {

    public Vector2[] upHemisphere (float hx, float hy) {
        List<Vector2> points = new ArrayList<>();
        System.out.println();
        float a = hx / 2;
        float b =  hy / 2;
        for(int i = 0 ; i <= 180; i += 25 ){
            double cosX = 0.0;
            double sinY = 0.0;
            if(i != 90 && i != 270)  cosX = cos(toRadians(i));
            if(i != 180 && i != 0) sinY = sin(toRadians(i));
            double x = a * cosX;
            double y = b * sinY;
            points.add(new Vector2((float) (x + a), (float) (y + b)));
        }
        return points.toArray(new Vector2[0]);
    }

    public Vector2[] downHemisphere (float hx, float hy) {
        List<Vector2> points = new ArrayList<>();
        System.out.println();
        float a = hx / 2;
        float b =  hy / 2;
        for(int i = 180 ; i <= 360; i += 25 ){
            double cosX = 0.0;
            double sinY = 0.0;
            if(i != 90 && i != 270)  cosX = cos(toRadians(i));
            if(i != 180 && i != 0) sinY = sin(toRadians(i));
            double x = a * cosX;
            double y = b * sinY;
            points.add(new Vector2((float) (x + a), (float) (y + b)));
        }
        return points.toArray(new Vector2[0]);
    }
}
