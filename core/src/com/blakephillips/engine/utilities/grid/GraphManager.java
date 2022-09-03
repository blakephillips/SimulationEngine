package com.blakephillips.engine.utilities.grid;


import com.badlogic.gdx.utils.Array;
import space.earlygrey.simplegraphs.Graph;
import space.earlygrey.simplegraphs.UndirectedGraph;

public class GraphManager {

    public Graph<Vertex> graph;
    private int graphHeight;
    private int graphWidth;

    public GraphManager(int graphWidth, int graphHeight) {

        this.graphWidth = graphWidth;
        this.graphHeight = graphHeight;

        graph = new UndirectedGraph<>();
        for (int x = 0; x < graphWidth; x++) {
            for (int y = 0; y < graphWidth; y++) {
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

            if (graph.contains(neighbor) && !graph.edgeExists(vertex, neighbor)) {
                graph.addEdge(vertex, neighbor);
            }
        }
    }
}



