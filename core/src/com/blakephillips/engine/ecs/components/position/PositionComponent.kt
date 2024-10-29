package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 pos;

    public PositionComponent(Vector2 pos) {
        this.pos = pos;
    }
}
