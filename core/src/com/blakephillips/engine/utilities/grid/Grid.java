package com.blakephillips.engine.utilities.grid;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;

public class Grid {

    public TiledMap tileMap;
    public OrthogonalTiledMapRenderer tileMapRenderer;

    private int width;
    private int height;
    private float cellSize;

    private OrthographicCamera camera;
    private TiledMapTileLayer collisionLayer;

    public Grid(int height, int width, int cellSize, float unitScale, OrthographicCamera camera) {
        this.height = height;
        this.width = width;
        this.cellSize = cellSize;
        this.camera = camera;

        tileMap = new TiledMap();

        SpriteSheet spriteSheet = new SpriteSheet("tileset_grassland.png", 16, 16);


        MapLayers layers = tileMap.getLayers();

        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, cellSize, cellSize);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(spriteSheet.getTextureFromTileMap(10,2)));
                layer.setCell(x, y, cell);
            }
        }
        layers.add(layer);

        collisionLayer = new TiledMapTileLayer(width, height, cellSize, cellSize);
        collisionLayer.setName("collision");
        layers.add(collisionLayer);

        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap, unitScale);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean obstacle(Vertex v) {
        return !(collisionLayer.getCell(v.x, v.y) == null);
    }

}
