package com.blakephillips.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.PathComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.grid.Vertex;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;

public class DebugSystem extends EntitySystem {
    //temp
    TextureRegion region;
    Entity testEntity;

    TileMap tileMap;
    public DebugSystem(TileMap tileMap, Entity testEntity) {
        this.tileMap = tileMap;
        this.testEntity = testEntity;
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

            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.map.getLayers().get("collision");

            Vertex vertex = tileMap.worldToCellIndex(v2pos);
            tileMap.graph.graph.disconnect(vertex);

            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(region));
            collision.setCell(vertex.x, vertex.y, cell);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();

            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.map.getLayers().get("collision");
            Vertex vertex = tileMap.worldToCellIndex(v2pos);

            tileMap.graph.connectNeighbors(vertex);
            collision.setCell(vertex.x, vertex.y, null);
        }
        //temporary pathfinding test
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();
            Vertex destination = tileMap.worldToCellIndex(v2pos);
            Vertex pos = tileMap.worldToCellIndex(testEntity.getComponent(PositionComponent.class).pos);
            testEntity.add(new PathComponent(Pathfinding.getPath(pos, destination, tileMap.graph.graph)));
        }

    }



}