package com.blakephillips.engine.ecs.components.position

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class OffsetPositionComponent(@JvmField var offsetX: Float, @JvmField var offsetY: Float) : Component {
    @JvmField
    var lastPosition: Vector2 = Vector2()
}
