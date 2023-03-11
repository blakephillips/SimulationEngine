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
import com.blakephillips.engine.ecs.components.ai.JobComponent;
import com.blakephillips.engine.ecs.components.ai.StateComponent;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.grid.Vertex;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;
import com.blakephillips.game.ai.states.DeferrableHaulState;
import com.blakephillips.game.ai.states.HaulState;
import com.blakephillips.game.ai.states.PathFindingState;
import com.blakephillips.game.data.JobType;
import com.blakephillips.game.data.ResourceType;
import com.blakephillips.game.data.UIState;
import com.blakephillips.game.ecs.components.JobTypeComponent;
import com.blakephillips.game.ecs.systems.QueueSystem;

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

    @Override
    public void update(float deltaTime) {
        if (Orchestrator.uiState != UIState.DEFAULT) { return; }
        Vector2 v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();
        //temporary wall creation to test A*
        if (Gdx.input.isTouched() && !Orchestrator.gameIgnoreInput) {
            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.map.getLayers().get("collision");

            Vertex vertex = tileMap.worldToCellIndex(v2pos);
            tileMap.graph.graph.disconnect(vertex);

            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(region));
            collision.setCell(vertex.x, vertex.y, cell);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !Orchestrator.gameIgnoreInput) {

            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.map.getLayers().get("collision");
            Vertex vertex = tileMap.worldToCellIndex(v2pos);

            tileMap.graph.connectNeighbors(vertex);
            collision.setCell(vertex.x, vertex.y, null);
        }
        //temporary pathfinding test
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && !Orchestrator.gameIgnoreInput) {
            Vertex destination = tileMap.worldToCellIndex(v2pos);

            PathFindingState walk = new PathFindingState(null, new Vector2(v2pos.x, v2pos.y));
            JobComponent job = new JobComponent("Walk", JobComponent.JobStatus.IDLE, walk);
            JobTypeComponent jobType = new JobTypeComponent(JobType.HAUL);
            Entity entity = new Entity();
            entity.add(job);
            entity.add(jobType);
            Orchestrator.engine.addEntity(entity);
        }
        //temporary hauling test
        if (Gdx.input.isKeyJustPressed(Input.Keys.H) && !Orchestrator.gameIgnoreInput) {
            testEntity.add(new StateComponent(new HaulState(testEntity, testHaulEntity, v2pos)));
        }

        //temporary hauling test
        if (Gdx.input.isKeyJustPressed(Input.Keys.C) && !Orchestrator.gameIgnoreInput) {
            Orchestrator.engine.getSystem(QueueSystem.class).clearQueue();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.J) && !Orchestrator.gameIgnoreInput) {
            DeferrableHaulState haul = new DeferrableHaulState(ResourceType.WOOD, v2pos);
            JobComponent job = new JobComponent("Move Wood", JobComponent.JobStatus.IDLE, haul);
            JobTypeComponent jobType = new JobTypeComponent(JobType.HAUL);
            Entity entity = new Entity();
            entity.add(job);
            entity.add(jobType);
            Orchestrator.engine.addEntity(entity);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && !Orchestrator.gameIgnoreInput) {
            PathFindingState walk = new PathFindingState(null, new Vector2(v2pos.x, v2pos.y));
            JobComponent jobComponent = new JobComponent("Walk", JobComponent.JobStatus.IDLE, walk);
            JobTypeComponent jobTypeComponent = new JobTypeComponent(JobType.HAUL);
            Entity entity = new Entity();
            entity.add(jobComponent);
            entity.add(jobTypeComponent);
            getEngine().addEntity(entity);
        }

    }



}
