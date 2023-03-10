package com.blakephillips.game.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.components.gfx.TextureComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.engine.utilities.sprite.SpriteSheet
import com.blakephillips.game.Orchestrator


class Tree(private val initialPos: Vector2?) {
    var pos: Vector2 = initialPos ?: Vector2(0F, 0F)
    var entity = Entity().also {
        it.add(PositionComponent(pos))
        val spr = SpriteSheet("tileset_grassland.png", 32, 48)
        it.add(TextureComponent(spr.getTextureFromTileMap(1, 4), 0))
        Orchestrator.getEngine().addEntity(it)
    }


}