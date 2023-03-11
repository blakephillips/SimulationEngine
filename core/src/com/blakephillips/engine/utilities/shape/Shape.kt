package com.blakephillips.engine.utilities.shape

import com.badlogic.gdx.math.Vector2

abstract class Shape {
    abstract fun getHeight(): Float
    abstract fun getWidth(): Float
    abstract fun origin(): Vector2
    abstract fun contains(pos: Vector2): Boolean
}