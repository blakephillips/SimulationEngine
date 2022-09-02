package com.blakephillips.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.blakephillips.engine.utilities.grid.Tile;

public class PathfindingComponent implements Component {
    public Tile destination;

    public PathfindingComponent(Tile destination) {
        this.destination = destination;
    }

}
