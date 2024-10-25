package com.blakephillips.engine.utilities.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class SpriteSheet {
    private final TextureRegion spriteSheet;
    private final TextureRegion[][] splitSpriteSheet;
    private final HashMap<String, TextureRegion> flippedSprites;

    private final int spriteWidth;
    private final int spriteHeight;

    public SpriteSheet(String assetPath, int spriteWidth, int spriteHeight) {
        spriteSheet = new TextureRegion(
                new Texture(Gdx.files.internal(assetPath))
        );
        splitSpriteSheet = spriteSheet.split(spriteWidth, spriteHeight);
        flippedSprites = new HashMap<>();

        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;

    }

    public TextureRegion getTextureFromTileMap(int col, int row) {
        return getTextureFromTileMap(col, row, false);
    }

    public TextureRegion getTextureFromTileMap(int col, int row, boolean flipped) {

        if (col > splitSpriteSheet.length - 1 || col < 0) {
            Gdx.app.error("SimEngine", String.format("Accessing tilemap using col '%x' is out of bounds of the tilemap.", col));
            return splitSpriteSheet[0][0];
        }

        if (row > splitSpriteSheet[col].length - 1 || row < 0) {
            Gdx.app.error("SimEngine", String.format("Accessing tilemap using row '%x' is out of bounds of the tilemap.", row));
            return splitSpriteSheet[0][0];
        }

        if (flipped) {
            if (flippedSprites.containsKey(getStringKey(col, row))) {
                return flippedSprites.get(getStringKey(col, row));
            }

            TextureRegion region = spriteSheet.split(spriteWidth, spriteHeight)[col][row];
            region.flip(true, false);
            flippedSprites.put(getStringKey(col, row), region);
            return region;
        }

        return splitSpriteSheet[col][row];
    }

    private String getStringKey(int col, int row) {
        return String.format("%x:%x", col, row);
    }

}
