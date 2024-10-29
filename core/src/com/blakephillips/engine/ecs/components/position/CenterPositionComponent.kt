package com.blakephillips.engine.ecs.components.position

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class CenterPositionComponent(@JvmField val width: Float, @JvmField val height: Float) : Component {
    @JvmField
    var lastPosition = Vector2()
}
