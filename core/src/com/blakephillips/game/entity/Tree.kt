package com.blakephillips.game.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.components.gfx.TextureComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.engine.utilities.sprite.SpriteSheet
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.JobType
import com.blakephillips.game.data.ResourceType
import com.blakephillips.game.ecs.components.DropItemComponent
import com.blakephillips.game.ecs.components.HarvestableComponent
import com.blakephillips.game.ecs.components.ResourceComponent
import com.blakephillips.game.ecs.components.SelectableComponent


class Tree(initialPos: Vector2?) {
    var pos: Vector2 = initialPos ?: Vector2(0F, 0F)
    var entity = Entity().also {
        val sprites = SpriteSheet("sprites.png", 16, 16)
        val logTex = sprites.getTextureFromTileMap(0, 0)
        val log = Entity()
        log.add(TextureComponent(logTex, -1))
        log.add(ResourceComponent(ResourceType.WOOD))


        val positionComponent = PositionComponent(pos)
        it.add(positionComponent)
        val spr = SpriteSheet("tileset_grassland.png", 32, 48)
        it.add(TextureComponent(spr.getTextureFromTileMap(1, 4), 0))
        it.add(SelectableComponent())
        it.add(HarvestableComponent(false))
        it.add(DropItemComponent(log))
        Orchestrator.engine.addEntity(it)
    }
}