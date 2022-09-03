package com.blakephillips.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.blakephillips.engine.utilities.grid.Vertex;

public class PathfindingComponent implements Component {
    public Vertex destination;

    public PathfindingComponent(Vertex destination) {
        this.destination = destination;
    }

}
