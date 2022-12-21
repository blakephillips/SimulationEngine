package com.blakephillips.engine.ecs.systems.gfx;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.blakephillips.engine.utilities.grid.TileMap;

public class TilemapRenderSystem extends EntitySystem {

    public TileMap tilemap;
    private float unitScale;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer renderer;

    public TilemapRenderSystem(OrthographicCamera camera, OrthogonalTiledMapRenderer renderer) {
        this.camera = camera;
        this.renderer = renderer;
    }

    @Override
    public void update(float deltaTime) {
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void addedToEngine (Engine engine) {
        Gdx.app.log("SimEngine", "TilemapRenderSystem added to engine.");
    }

    @Override
    public void removedFromEngine (Engine engine) {
        Gdx.app.log("SimEngine", "MovementSystem removed from engine.");
    }







}
