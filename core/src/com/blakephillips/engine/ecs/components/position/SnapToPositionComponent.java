package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;

public class SnapToPositionComponent implements Component {
    public PositionComponent posComponent;
    public SnapToPositionComponent(PositionComponent posComponent) {
        this.posComponent = posComponent;
    }
}
