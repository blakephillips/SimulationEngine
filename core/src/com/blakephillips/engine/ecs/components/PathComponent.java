package com.blakephillips.engine.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.utilities.grid.Vertex;
import space.earlygrey.simplegraphs.Path;

public class PathComponent implements Component {
    public Path<Vertex> path;
    public Vector2 destination;

    public PathComponent(Path<Vertex> path) {
        this.path = path;
    }
    public PathComponent(Vector2 destination) {
        this.destination = destination;
    }

}
