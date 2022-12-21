package com.blakephillips.engine.utilities.grid;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;

public class TileMap {

    public TiledMap map;
    public OrthogonalTiledMapRenderer tileMapRenderer;
    public Graph graph;

    private final int width;
    private final int height;
    private final float cellSize;

    private final OrthographicCamera camera;
    private final TiledMapTileLayer collisionLayer;

    public TileMap(int height, int width, int cellSize, float unitScale, OrthographicCamera camera) {
        this.height = height;
        this.width = width;
        this.cellSize = cellSize;
        this.camera = camera;

        map = new TiledMap();

        SpriteSheet spriteSheet = new SpriteSheet("tileset_grassland.png", 16, 16);


        MapLayers layers = map.getLayers();

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

        tileMapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
        graph = new Graph(this, width, height);
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

    public Vertex worldToCellIndex(Vector2 pos) {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        int x = (int) Math.floor(pos.x / layer.getTileWidth());
        int y = (int) Math.floor(pos.y / layer.getTileHeight());

        return new Vertex(x, y);
    }

    public Vector2 cellIndexToWorld(Vertex vertex) {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);

        return new Vector2(vertex.x * layer.getTileWidth(), vertex.y * layer.getTileHeight());
    }
}
