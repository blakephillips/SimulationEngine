package com.blakephillips.engine.utilities.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import space.earlygrey.simplegraphs.Graph;
import space.earlygrey.simplegraphs.Path;

public class Pathfinding {

    //Using Chebyshev distance as a heuristic
    public static Path<Vertex> getPath(Vertex start, Vertex target, Graph<Vertex> vertexGraph) {
        Gdx.app.log("Pathfinding", "getPath called");
        return vertexGraph.algorithms().findShortestPath(start, target, (a, b) -> Float.max(b.x - a.x, b.y - a.y));
    }

    public static Path<Vertex> getPath(Vector2 start, Vector2 target, TileMap tileMap) {
        return getPath(tileMap.worldToCellIndex(start), tileMap.worldToCellIndex(target), tileMap.graph.getGraph());
    }

    public static boolean isReachable(Vertex start, Vertex target, Graph<Vertex> vertexGraph) {
        return !getPath(start, target, vertexGraph).isEmpty();
    }

    public static boolean isReachable(Vector2 start, Vector2 target, TileMap tileMap) {
        return isReachable(tileMap.worldToCellIndex(start), tileMap.worldToCellIndex(target), tileMap.graph.getGraph());
    }

    public static float chebyshevDistance(Vector2 p1, Vector2 p2) {
        return Float.max(p2.x - p1.x, p2.y - p1.y);
    }

}


