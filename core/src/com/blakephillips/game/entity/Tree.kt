package com.blakephillips.game.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.components.gfx.TextureComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.engine.utilities.sprite.SpriteSheet
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.ObjectType
import com.blakephillips.game.data.ResourceType
import com.blakephillips.game.ecs.components.*


class Tree(initialPos: Vector2 = Vector2(0F, 0F)) {
    val entity = Entity().apply {
        val log = Entity().apply {
            add(TextureComponent(logTexture, -1))
            add(ResourceComponent(ResourceType.WOOD))
        }
        add(DropItemComponent(log))
        add(PositionComponent(initialPos))
        add(TextureComponent(treeTexture, 0))
        add(SelectableComponent())
        add(HarvestableComponent(false))
        add(ObjectTypeComponent(ObjectType.TREE))
        Orchestrator.engine.addEntity(this)
    }

    private companion object {
        val sprites = SpriteSheet("sprites.png", 16, 16)
        val tileset = SpriteSheet("tileset_grassland.png", 32, 48)

        val logTexture = sprites.getTextureFromTileMap(0, 0)
        val treeTexture = tileset.getTextureFromTileMap(1, 4)
    }
}