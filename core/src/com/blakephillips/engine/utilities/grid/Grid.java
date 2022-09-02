package com.blakephillips.engine.utilities.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;

public class Grid {

    public TiledMap tileMap;
    public OrthogonalTiledMapRenderer tileMapRenderer;

    private int width;
    private int height;
    private float cellSize;

    private OrthographicCamera camera;

    private Array<Array<Tile>> tileGrid;

    public Grid(int height, int width, int cellSize, float unitScale, OrthographicCamera camera) {
        this.height = height;
        this.width = width;
        this.cellSize = cellSize;
        this.camera = camera;

        tileMap = new TiledMap();

        SpriteSheet spriteSheet = new SpriteSheet("tileset_grassland.png", 16, 16);


        MapLayers layers = tileMap.getLayers();

        tileGrid = new Array<>(width);

        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, cellSize, cellSize);
        for (int x = 0; x < width; x++) {
//            tileGrid.add(new Array<Tile>(height));

            for (int y = 0; y < height; y++) {
//                Tile tile = new Tile(x,y);
//                tileGrid.get(x).add(tile);

                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(spriteSheet.getTextureFromTileMap(10,2)));
                layer.setCell(x, y, cell);
            }
        }
        layers.add(layer);

        TiledMapTileLayer collision = new TiledMapTileLayer(width, height, cellSize, cellSize);
        collision.setName("collision");
        layers.add(collision);

        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap, unitScale);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Tile GetTileFromPosition(Vector2 worldPosition) {
        int x = (int)Math.floor(worldPosition.x / cellSize);
        int y = (int)Math.floor(worldPosition.y / cellSize);

        return getTileByIndex(x, y);
    }

    public void render(float deltaTime) {
        tileMapRenderer.setView(camera);
        tileMapRenderer.render();
    }

    private Tile getTileByIndex(int x, int y) {
        if (x < 0 || x >= tileGrid.size) {
            Gdx.app.error("SimEngine",
                    String.format("Out of bounds accessing tile X index: '%x'", x));
            return null;
        }
        if (y < 0 || x >= tileGrid.get(x).size) {
            Gdx.app.error("SimEngine",
                    String.format("Out of bounds accessing tile X index: '%x'", x));
            return null;
        }
        return tileGrid.get(x).get(y);
    }
}
