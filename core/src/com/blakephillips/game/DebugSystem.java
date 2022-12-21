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
import com.blakephillips.engine.ecs.components.ai.JobComponent;
import com.blakephillips.engine.ecs.components.ai.StateComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.grid.Vertex;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;
import com.blakephillips.game.ai.states.HaulState;
import com.blakephillips.game.ai.jobs.GetResourceTypeToDestination;
import com.blakephillips.game.ai.states.PathFindingState;
import com.blakephillips.game.data.ResourceType;

public class DebugSystem extends EntitySystem {
    //temp
    TextureRegion region;
    Entity testEntity;
    Entity testHaulEntity;

    TileMap tileMap;
    public DebugSystem(TileMap tileMap, Entity testEntity, Entity testHaulEntity, Entity testOtherEntity) {
        this.tileMap = tileMap;
        //temporary
        this.testEntity = testEntity;
        this.testHaulEntity = testHaulEntity;
        SpriteSheet spriteSheet = new SpriteSheet("tileset_grassland.png", 16, 16);
        region = spriteSheet.getTextureFromTileMap(0, 8);
    }

    private Vector2 v2pos;
    @Override
    public void update(float deltaTime) {
        v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();
        //temporary wall creation to test A*
        if (Gdx.input.isTouched()) {
            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.map.getLayers().get("collision");

            Vertex vertex = tileMap.worldToCellIndex(v2pos);
            tileMap.graph.graph.disconnect(vertex);

            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(region));
            collision.setCell(vertex.x, vertex.y, cell);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {

            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.map.getLayers().get("collision");
            Vertex vertex = tileMap.worldToCellIndex(v2pos);

            tileMap.graph.connectNeighbors(vertex);
            collision.setCell(vertex.x, vertex.y, null);
        }
        //temporary pathfinding test
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            Vertex destination = tileMap.worldToCellIndex(v2pos);
            Vertex pos = tileMap.worldToCellIndex(testEntity.getComponent(PositionComponent.class).pos);
            testEntity.add(new PathComponent(Pathfinding.getPath(pos, destination, tileMap.graph.graph)));
        }
        //temporary hauling test
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            testEntity.add(new StateComponent(new HaulState(testEntity, testHaulEntity, v2pos)));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            new GetResourceTypeToDestination(testEntity, v2pos, ResourceType.WOOD);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            PathFindingState walk = new PathFindingState(testEntity, v2pos);
            JobComponent jobComponent = new JobComponent("Walk", JobComponent.JobStatus.START_PENDING, walk);
            Entity entity = new Entity();
            entity.add(jobComponent);
            getEngine().addEntity(entity);
        }

    }



}
