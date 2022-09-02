package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class CenterPositionComponent implements Component {

    public Vector2 lastPosition;

    public CenterPositionComponent(Vector2 lastPosition) {
        this.lastPosition = lastPosition;
    }

}
