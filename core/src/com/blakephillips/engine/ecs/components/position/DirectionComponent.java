package com.blakephillips.engine.ecs.components.position;

import com.badlogic.ashley.core.Component;
import com.blakephillips.engine.utilities.Direction;

public class DirectionComponent implements Component {
    private Direction direction;

    public DirectionComponent(Direction direction) {
        setDirection(direction);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
