package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class OffsetPositionComponent implements Component {
    public float offsetX;
    public float offsetY;
    public Vector2 lastPosition;

    public OffsetPositionComponent(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        lastPosition = new Vector2(0, 0);
    }
}
