package com.blakephillips.engine.utilities.sprite

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SpriteSheet(assetPath: String, private val spriteWidth: Int, private val spriteHeight: Int) {
    private val spriteSheet: TextureRegion = TextureRegion(
        Texture(Gdx.files.internal(assetPath))
    )
    private val splitSpriteSheet = spriteSheet.split(spriteWidth, spriteHeight)
    private val flippedSprites: HashMap<String, TextureRegion> = HashMap()


    fun getTextureFromTileMap(col: Int, row: Int): TextureRegion {
        return getTextureFromTileMap(col, row, false)
    }

    fun getTextureFromTileMap(col: Int, row: Int, flipped: Boolean): TextureRegion {
        if (col > splitSpriteSheet.size - 1 || col < 0) {
            Gdx.app.error(
                "SimEngine",
                String.format("Accessing tilemap using col '%x' is out of bounds of the tilemap.", col)
            )
            return splitSpriteSheet[0][0]
        }
        if (row > splitSpriteSheet[col].size - 1 || row < 0) {
            Gdx.app.error(
                "SimEngine",
                String.format("Accessing tilemap using row '%x' is out of bounds of the tilemap.", row)
            )
            return splitSpriteSheet[0][0]
        }
        if (flipped) {
            // get flipped sprite from map if exists
            val flippedSprite = flippedSprites[getStringKey(col, row)]
            if (flippedSprite !== null) return flippedSprite

            // if it doesn't exist in map, get the sprite, flip it and cache in map
            val region = spriteSheet.split(spriteWidth, spriteHeight)[col][row]
            region.flip(true, false)
            flippedSprites[getStringKey(col, row)] = region
            return region
        }
        return splitSpriteSheet[col][row]
    }

    private fun getStringKey(col: Int, row: Int): String {
        return String.format("%x:%x", col, row)
    }
}
