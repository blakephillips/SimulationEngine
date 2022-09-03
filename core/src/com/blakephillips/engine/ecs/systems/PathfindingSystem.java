package com.blakephillips.engine.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.TextComponent;
import com.blakephillips.engine.ecs.components.position.OffsetPositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.utilities.grid.GraphManager;
import com.blakephillips.engine.utilities.grid.Vertex;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;
import space.earlygrey.simplegraphs.Path;

public class PathfindingSystem extends EntitySystem {

    public GraphManager graphManager;

    //temp
    TextureRegion region;

    TiledMap tileMap;
    public PathfindingSystem(TiledMap tileMap, GraphManager graphManager) {
        this.tileMap = tileMap;
        this.graphManager = graphManager;

        //temporary
        SpriteSheet spriteSheet = new SpriteSheet("tileset_grassland.png", 16, 16);
        region = spriteSheet.getTextureFromTileMap(0, 8);
    }

    private Vector2 v2pos;
    @Override
    public void update(float deltaTime) {


        //temporary wall creation to test A*
        if (Gdx.input.isTouched()) {
            v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();

            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.getLayers().get("collision");

            Vertex vertex = worldToCellIndex(v2pos);
            graphManager.graph.removeEdges(graphManager.graph.getEdges(vertex));

            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(region));
            collision.setCell((int)vertex.x, (int)vertex.y, cell);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();

            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.getLayers().get("collision");
            Vertex vertex = worldToCellIndex(v2pos);

            graphManager.connectNeighbors(vertex);


            collision.setCell(vertex.x, vertex.y, null);

        }
        //temporary pathfinding test
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();
            Vertex destination = worldToCellIndex(v2pos);

            Path<Vertex> path = graphManager.graph.algorithms().findShortestPath(new Vertex(0,0), destination);
            for (Vertex v: path) {
                Entity entity = new Entity();
                entity.add(new PositionComponent(new Vector2(cellIndexToWorld(v))));
                entity.add(new OffsetPositionComponent(10, 10));
                entity.add(new TextComponent("x"));
                getEngine().addEntity(entity);
            }

        }

    }

    public Vertex worldToCellIndex(Vector2 pos) {

        TiledMapTileLayer layer = (TiledMapTileLayer)tileMap.getLayers().get(0);

        int x = (int) Math.floor(pos.x / layer.getTileWidth());
        int y = (int) Math.floor(pos.y / layer.getTileHeight());

        return new Vertex(x, y);
    }

    public Vector2 cellIndexToWorld(Vertex vertex) {
        TiledMapTileLayer layer = (TiledMapTileLayer)tileMap.getLayers().get(0);

        return new Vector2(vertex.x * layer.getTileWidth(), vertex.y * layer.getTileHeight());
    }

}
