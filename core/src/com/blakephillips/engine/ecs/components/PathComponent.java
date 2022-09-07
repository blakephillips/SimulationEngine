package com.blakephillips.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.blakephillips.engine.utilities.grid.Vertex;
import space.earlygrey.simplegraphs.Path;

public class PathComponent implements Component {
    public Path<Vertex> path;

    public PathComponent(Path<Vertex> path) {
        this.path = path;
    }

}
