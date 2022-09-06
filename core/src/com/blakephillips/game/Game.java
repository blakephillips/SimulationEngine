package com.blakephillips.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.blakephillips.engine.ecs.systems.PathfindingSystem;
import com.blakephillips.engine.ecs.systems.gfx.RenderSystem;
import com.blakephillips.engine.ecs.systems.gfx.TextRenderSystem;
import com.blakephillips.engine.ecs.systems.gfx.TilemapRenderSystem;
import com.blakephillips.engine.ecs.systems.mouse.FollowMousePositionSystem;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.ecs.systems.position.CenterPositionSystem;
import com.blakephillips.engine.ecs.systems.position.MovementSystem;
import com.blakephillips.engine.ecs.systems.position.OffsetPositionSystem;
import com.blakephillips.engine.ecs.systems.position.SnapPositionSystem;
import com.blakephillips.engine.utilities.grid.GraphManager;
import com.blakephillips.engine.utilities.grid.Grid;
import com.blakephillips.game.ui.TileSelector;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Engine engine;
	OrthographicCamera camera;
	Viewport viewport;

	Grid grid;

	@Override
	public void create () {
		camera = new OrthographicCamera(680, 480);
		camera.position.set(320, 240, 0);
		camera.setToOrtho(false, 680, 480);
		viewport = new FitViewport(640, 480, camera);

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		engine = new Engine();
		batch.enableBlending();

		int gridHeight = 300;
		int gridWidth = 300;

		grid = new Grid(gridHeight, gridWidth, 16, 1, camera);
		engine.addSystem(new TilemapRenderSystem(camera, grid.tileMapRenderer));
		engine.addSystem(new RenderSystem(batch));
		engine.addSystem(new TextRenderSystem(batch));
		engine.addSystem(new FollowMousePositionSystem());
		engine.addSystem(new MousePositionSystem(viewport));
		engine.addSystem(new CenterPositionSystem());
		engine.addSystem(new SnapPositionSystem());
		engine.addSystem(new OffsetPositionSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new PathfindingSystem(grid.tileMap, new GraphManager(grid, gridWidth, gridHeight)));

		new TileSelector(engine);
		new Character(new Vector2(30, 30), engine);
		new Character(new Vector2(50, 50), engine);
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
