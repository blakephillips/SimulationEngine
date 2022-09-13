package com.blakephillips.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.blakephillips.engine.ecs.components.ai.JobComponent;
import com.blakephillips.engine.ecs.components.gfx.DisplayFpsComponent;
import com.blakephillips.engine.ecs.components.gfx.TextComponent;
import com.blakephillips.engine.ecs.components.gfx.TextureComponent;
import com.blakephillips.engine.ecs.components.ai.StateComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.systems.PathFollowingSystem;
import com.blakephillips.engine.ecs.systems.ai.JobSystem;
import com.blakephillips.engine.ecs.systems.ai.StateSystem;
import com.blakephillips.engine.ecs.systems.gfx.RenderSystem;
import com.blakephillips.engine.ecs.systems.gfx.TextRenderSystem;
import com.blakephillips.engine.ecs.systems.gfx.TilemapRenderSystem;
import com.blakephillips.engine.ecs.systems.mouse.FollowMousePositionSystem;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.ecs.systems.position.*;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;
import com.blakephillips.game.ai.HaulState;
import com.blakephillips.game.ai.PathFindingState;
import com.blakephillips.game.ui.TileSelector;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Engine engine;
	OrthographicCamera camera;
	Viewport viewport;

	TileMap tilemap;

	@Override
	public void create () {
		camera = new OrthographicCamera(680, 480);
		camera.position.set(320, 240, 0);
		camera.setToOrtho(false, 680, 480);
		viewport = new FitViewport(640, 480, camera);

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		engine = Orchestrator.getEngine();
		batch.enableBlending();

		int gridHeight = 300;
		int gridWidth = 300;

		Character character = new Character(new Vector2(5, 5), engine);
		tilemap = new TileMap(gridHeight, gridWidth, 16, 1, camera);
		engine.addSystem(new TilemapRenderSystem(camera, tilemap.tileMapRenderer));
		engine.addSystem(new RenderSystem(batch));
		engine.addSystem(new TextRenderSystem(batch));
		engine.addSystem(new FollowMousePositionSystem());
		engine.addSystem(new MousePositionSystem(viewport));
		engine.addSystem(new CenterPositionSystem());
		engine.addSystem(new SnapPositionSystem());
		engine.addSystem(new OffsetPositionSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new PathFollowingSystem(tilemap));
		engine.addSystem(new StateSystem());
		engine.addSystem(new TextureDirectionSystem());
		new TileSelector(engine);

		//display fps
		Entity fps = new Entity();
		fps.add(new PositionComponent(new Vector2(30, 455)));
		fps.add(new TextComponent(""));
		fps.add(new DisplayFpsComponent());
		engine.addEntity(fps);

		//testing hauling
		Entity c = new Character(new Vector2(50, 50), engine).entity;
		Entity haulObject = new Entity();
		SpriteSheet spriteSheet = new SpriteSheet("tileset_grassland.png", 16, 16);
		TextureRegion spr = spriteSheet.getTextureFromTileMap(0, 8);

		haulObject.add(new TextureComponent(spr, 0));
		haulObject.add(new PositionComponent(new Vector2(250, 250)));

		engine.addEntity(haulObject);
		//job system testing
		HaulState haulState = new HaulState(c, haulObject, new Vector2(50, 50));
		PathFindingState pathState = new PathFindingState(c, new Vector2(450, 450));
		PathFindingState pathState2 = new PathFindingState(c, new Vector2(300, 300));
		HaulState haulState2 = new HaulState(c, haulObject, new Vector2(450, 450));
		haulState.setNextState(pathState);
		pathState.setNextState(pathState2);
		pathState2.setNextState(haulState2);
		//c.add(new StateComponent(haulState));

		JobComponent jobComponent = new JobComponent("Haul to place", JobComponent.JobStatus.START_PENDING, haulState);
		Entity jobEntity = new Entity();
		jobEntity.add(jobComponent);
		engine.addEntity(jobEntity);

		engine.addSystem(new JobSystem());
		engine.addSystem(new DebugSystem(tilemap, character.entity, haulObject));

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.setTransformMatrix(camera.view);
		camera.update();
		batch.begin();
		engine.update(Gdx.graphics.getDeltaTime());
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.update();
	}

}
