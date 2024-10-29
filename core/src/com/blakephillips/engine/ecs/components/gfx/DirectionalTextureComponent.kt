package com.blakephillips.engine.ecs.components.gfx

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.blakephillips.engine.utilities.Direction

class DirectionalTextureComponent(
    private val north: TextureRegion, private val east: TextureRegion,
    private val south: TextureRegion, private val west: TextureRegion
) : Component {
    fun getTexture(direction: Direction): TextureRegion =
        when (direction) {
            Direction.NORTH -> north
            Direction.EAST -> east
            Direction.SOUTH -> south
            Direction.WEST -> west
        }
}
