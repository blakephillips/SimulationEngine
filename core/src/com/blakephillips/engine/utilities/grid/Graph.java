package com.blakephillips.engine.utilities.grid;


import com.badlogic.gdx.utils.Array;
import space.earlygrey.simplegraphs.UndirectedGraph;

public class Graph {

    public UndirectedGraph<Vertex> graph;
    private int graphHeight;
    private int graphWidth;
    private TileMap tilemap;

    public Graph(TileMap tilemap, int graphWidth, int graphHeight) {

        this.tilemap = tilemap;

        this.graphWidth = graphWidth;
        this.graphHeight = graphHeight;

        graph = new UndirectedGraph<>();
        for (int x = 0; x < graphWidth; x++) {
            for (int y = 0; y < graphHeight; y++) {
                Vertex v = new Vertex(x, y);

                graph.addVertex(v);
                connectNeighbors(v);
            }
        }
    }

    public void connectNeighbors(Vertex vertex) {
        int x = vertex.x;
        int y = vertex.y;

        Array<Vertex> directions = new Array<>(4);

        //connection directions
        directions.add(new Vertex(x, y+1));
        directions.add(new Vertex(x, y-1));
        directions.add(new Vertex(x+1, y));
        directions.add(new Vertex(x-1, y));

        for (Vertex neighbor: directions) {

            if (graph.contains(neighbor) && !graph.edgeExists(vertex, neighbor)
                && !tilemap.obstacle(neighbor)) {
                graph.addEdge(vertex, neighbor);
            }
        }
    }
}



