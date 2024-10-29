package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;

public class SnapPositionComponent implements Component {
    public int snapX;
    public int snapY;

    public SnapPositionComponent(int snapX, int snapY) {
        this.snapX = snapX;
        this.snapY = snapY;
    }
}
