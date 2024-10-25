package com.blakephillips.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.ai.JobComponent;
import com.blakephillips.engine.ecs.components.gfx.DisplayFpsComponent;
import com.blakephillips.engine.ecs.components.gfx.TextComponent;
import com.blakephillips.engine.ecs.components.gfx.TextureComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.systems.PathFollowingSystem;
import com.blakephillips.engine.ecs.systems.ai.JobSystem;
import com.blakephillips.engine.ecs.systems.ai.StateSystem;
import com.blakephillips.engine.ecs.systems.gfx.RenderSystem;
import com.blakephillips.engine.ecs.systems.gfx.ShapeRenderSystem;
import com.blakephillips.engine.ecs.systems.gfx.TextRenderSystem;
import com.blakephillips.engine.ecs.systems.gfx.TilemapRenderSystem;
import com.blakephillips.engine.ecs.systems.mouse.FollowMousePositionSystem;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.ecs.systems.position.*;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;
import com.blakephillips.game.ai.states.HaulState;
import com.blakephillips.game.ai.states.PathFindingState;
import com.blakephillips.game.ai.states.PathToResourceTypeState;
import com.blakephillips.game.data.JobStatus;
import com.blakephillips.game.data.JobType;
import com.blakephillips.game.data.ResourceType;
import com.blakephillips.game.ecs.components.JobTypeComponent;
import com.blakephillips.game.ecs.components.ResourceComponent;
import com.blakephillips.game.ecs.systems.InputSystem;
import com.blakephillips.game.ecs.systems.QueueSystem;
import com.blakephillips.game.ecs.systems.ResourceSystem;
import com.blakephillips.game.entity.Axe;
import com.blakephillips.game.entity.Tree;
import com.blakephillips.game.stage.GameUserInterfaceStage;
import com.blakephillips.game.ui.TileSelector;

public class Game extends ApplicationAdapter {
    SpriteBatch batch;
    Engine engine;

    TileMap tilemap;
    GameUserInterfaceStage gameUserInterfaceStage;

    @Override
    public void create() {

        gameUserInterfaceStage = new GameUserInterfaceStage(Orchestrator.viewport);

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        engine = Orchestrator.engine;
        batch.enableBlending();

        int gridHeight = 300;
        int gridWidth = 300;

        Character character = new Character(new Vector2(5, 5), engine);
        tilemap = new TileMap(gridHeight, gridWidth, 16, 1, Orchestrator.camera);
        engine.addSystem(new TilemapRenderSystem(Orchestrator.camera, tilemap.tileMapRenderer));
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new TextRenderSystem(batch));
        engine.addSystem(new FollowMousePositionSystem());
        engine.addSystem(new MousePositionSystem(Orchestrator.viewport));
        engine.addSystem(new CenterPositionSystem());
        engine.addSystem(new SnapPositionSystem());
        engine.addSystem(new OffsetPositionSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new PathFollowingSystem(tilemap));
        engine.addSystem(new StateSystem());
        engine.addSystem(new TextureDirectionSystem());
        engine.addSystem(new ResourceSystem());
        engine.addSystem(new JobSystem());
        engine.addSystem(new QueueSystem());
        engine.addSystem(new ShapeRenderSystem(batch));
        engine.addSystem(new InputSystem());
        new TileSelector();

        //display fps
        Entity fps = new Entity();
        fps.add(new PositionComponent(new Vector2(30, 455)));
        fps.add(new TextComponent(""));
        fps.add(new DisplayFpsComponent());
        engine.addEntity(fps);

        // don't look below, this is gross random testing

        new Axe(new Vector2(50, 50));

        //testing hauling
        Entity c = new Character(new Vector2(50, 50), engine).entity;
        Entity haulObject = new Entity();
        SpriteSheet spriteSheet = new SpriteSheet("tileset_grassland.png", 16, 16);
        TextureRegion spr = spriteSheet.getTextureFromTileMap(0, 8);
        haulObject.add(new TextureComponent(spr, 0));
        haulObject.add(new PositionComponent(new Vector2(250, 250)));

        engine.addEntity(haulObject);
        //job system testing
        HaulState haulState = new HaulState(null, haulObject, new Vector2(50, 50));
        PathFindingState pathState = new PathFindingState(null, new Vector2(450, 450));
        PathFindingState pathState2 = new PathFindingState(null, new Vector2(300, 300));
        HaulState haulState2 = new HaulState(null, haulObject, new Vector2(450, 450));
        haulState.setNextState(pathState);
        pathState.setNextState(pathState2);
        pathState2.setNextState(haulState2);

        JobComponent jobComponent = new JobComponent("Haul to place", JobStatus.IDLE, haulState);
        Entity jobEntity = new Entity();
        jobEntity.add(jobComponent);
        jobEntity.add(new JobTypeComponent(JobType.HAUL));
        engine.addEntity(jobEntity);

        // adding two things to the queue

        PathFindingState pathState1 = new PathFindingState(null, new Vector2(450, 450));
        JobComponent jobComponent2 = new JobComponent("Walk somewhere or something", JobStatus.IDLE, pathState1);
        Entity jobEntity2 = new Entity();
        jobEntity2.add(jobComponent2);
        jobEntity2.add(new JobTypeComponent(JobType.HAUL));
        engine.addEntity(jobEntity2);

        Entity jobEntity1 = new Entity();
        PathToResourceTypeState toResourceTypeState = new PathToResourceTypeState(null, ResourceType.WOOD);
        JobComponent jobComponent1 = new JobComponent("Haul other thing to place", JobStatus.IDLE, toResourceTypeState);
        jobEntity1.add(new JobTypeComponent(JobType.HAUL));
        jobEntity1.add(jobComponent1);
        engine.addEntity(jobEntity1);

        //item testing
        SpriteSheet sprites = new SpriteSheet("sprites.png", 16, 16);
        TextureRegion logTex = sprites.getTextureFromTileMap(0, 0);
        Entity log = new Entity();
        log.add(new TextureComponent(logTex, -1));
        log.add(new PositionComponent(new Vector2(16 * 15, 16 * 15)));
        log.add(new ResourceComponent(ResourceType.WOOD));
        engine.addEntity(log);

        Entity log2 = new Entity();
        log2.add(new TextureComponent(logTex, -1));
        log2.add(new PositionComponent(new Vector2(16 * 10, 16 * 10)));
        log2.add(new ResourceComponent(ResourceType.WOOD));
        engine.addEntity(log2);

        //tree
        new Tree(new Vector2(16 * 8, 16 * 8));
        new Tree(new Vector2(23 * 3, 23 * 3));
        new Tree(new Vector2(400, 400));
        new Axe(new Vector2(300, 300));


        engine.addSystem(new DebugSystem(tilemap, character.entity, haulObject, c));


    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(Orchestrator.camera.combined);
        batch.setTransformMatrix(Orchestrator.camera.view);
        Orchestrator.camera.update();
        batch.begin();
        engine.update(Gdx.graphics.getDeltaTime());
        batch.end();
        gameUserInterfaceStage.stage.act(Gdx.graphics.getDeltaTime());
        gameUserInterfaceStage.stage.draw();
    }

    @Override
    public void dispose() {
        gameUserInterfaceStage.stage.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        Orchestrator.viewport.update(width, height);
        Orchestrator.camera.update();
    }

}
