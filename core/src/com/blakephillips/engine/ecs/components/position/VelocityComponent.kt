package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component {
    public float dx;
    public float dy;

    public VelocityComponent(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
