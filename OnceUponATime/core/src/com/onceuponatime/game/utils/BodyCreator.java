package com.onceuponatime.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.onceuponatime.game.model.EllipseShape;
import com.onceuponatime.game.model.OctagonShape;

import java.util.Arrays;

public class BodyCreator {

    public static Body createPolygonBody (World world, Vector2 position, Vector2[] points, BodyDef.BodyType type) {
        BodyDef def = new BodyDef();
        def.fixedRotation = true;
        def.type = type;
        Body body = world.createBody(def);
        PolygonShape polygon = new PolygonShape();
        polygon.set(points);
        body.createFixture(polygon, 1f);
        body.setTransform(position.x, position.y, 0);
        polygon.dispose();
        return body;
    }

    public static void createStaticEllipseBody (World world, float x , float y, float weight, float height) {
        EllipseShape ellipseShape = new EllipseShape(weight, height);
        BodyDef def = new BodyDef();
        def.fixedRotation = true;
        ellipseShape.setUpHemispherePoints();
        def.type = BodyDef.BodyType.StaticBody;
        Body upBody = world.createBody(def);
        Body downBody = world.createBody(def);
        upBody.createFixture(ellipseShape, 1f);
        ellipseShape.setDownHemispherePoints();
        downBody.createFixture(ellipseShape, 1f);
        upBody.setTransform(x, y, 0);
        downBody.setTransform(x, y, 0);
        ellipseShape.dispose();
    }

    public static void createCircleBody(World world, float x, float y, float radius, BodyDef.BodyType type){
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);
        BodyDef def = new BodyDef();
        def.type = type;
        def.fixedRotation = true;
        Body body = world.createBody(def);
        body.createFixture(circle, 1f);
        body.setTransform(x, y, 0);
        circle.dispose();
    }

    public static Body createOctagonBody(World world, float x , float y, float weight, float height, BodyDef.BodyType type){
        OctagonShape octagonShape = new OctagonShape(weight, height);
        BodyDef def = new BodyDef();
        def.fixedRotation = true;
        def.type = type;
        Body body = world.createBody(def);
        body.createFixture(octagonShape, 1f);
        body.setTransform(x, y, 0);
        return body;
    }
}
