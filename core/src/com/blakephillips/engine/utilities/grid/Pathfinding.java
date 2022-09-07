package com.blakephillips.engine.utilities.grid;

import space.earlygrey.simplegraphs.Graph;
import space.earlygrey.simplegraphs.Path;

public class Pathfinding {

    //Using Chebyshev distance as a heuristic
    public static Path<Vertex> getPath(Vertex start, Vertex target, Graph<Vertex> vertexGraph) {
        return vertexGraph.algorithms().findShortestPath(start, target, (a, b) -> Float.max(b.x - a.x, b.y - a.y));
    }

    public static boolean isReachable(Vertex start, Vertex target, Graph<Vertex> vertexGraph) {
        return (getPath(start, target, vertexGraph).getLength() > 0);
    }
}


