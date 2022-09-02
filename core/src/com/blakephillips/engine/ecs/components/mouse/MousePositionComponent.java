package com.blakephillips.engine.ecs.components.mouse;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MousePositionComponent implements Component {
    public Vector2 pos;

    public MousePositionComponent(Vector2 pos) {
        this.pos = pos;
    }
}
