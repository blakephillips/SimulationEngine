package com.blakephillips.game.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.components.gfx.TextureComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.engine.utilities.sprite.SpriteSheet
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.ResourceType
import com.blakephillips.game.ecs.components.ResourceComponent

class Axe(initialPos: Vector2) {
    val entity = Entity().apply {
        add(PositionComponent(initialPos))
        add(TextureComponent(axeTexture, 0))
        add(ResourceComponent(ResourceType.AXE))
        Orchestrator.engine.addEntity(this)
    }
    private companion object {
        val sprites = SpriteSheet("sprites.png", 16, 16)
        val axeTexture = sprites.getTextureFromTileMap(0, 1)
    }
}