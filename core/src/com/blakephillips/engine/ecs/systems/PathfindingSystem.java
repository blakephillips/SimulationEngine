package com.blakephillips.engine.ecs.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;

public class PathfindingSystem extends EntitySystem {

    //temp
    TextureRegion region;

    TiledMap tileMap;
    public PathfindingSystem(TiledMap tileMap) {
        this.tileMap = tileMap;

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

            v2pos = worldToCellIndex(v2pos);

            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(region));
            collision.setCell((int)v2pos.x, (int)v2pos.y, cell);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            v2pos = getEngine().getSystem(MousePositionSystem.class).unprojectedMousePos();

            TiledMapTileLayer collision = (TiledMapTileLayer)tileMap.getLayers().get("collision");
            v2pos = worldToCellIndex(v2pos);


            collision.setCell((int)v2pos.x, (int)v2pos.y, null);

        }
    }

    public Vector2 worldToCellIndex(Vector2 pos) {

        TiledMapTileLayer layer = (TiledMapTileLayer)tileMap.getLayers().get(0);

        int x = (int) Math.floor(pos.x / layer.getTileWidth());
        int y = (int) Math.floor(pos.y / layer.getTileHeight());

        return new Vector2(x, y);
    }

}
