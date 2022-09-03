package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class CenterPositionComponent implements Component {

    public Vector2 lastPosition;
    public float width;
    public float height;

    public CenterPositionComponent(float width, float height) {
        lastPosition = new Vector2();
        this.width = width;
        this.height = height;
    }

}
