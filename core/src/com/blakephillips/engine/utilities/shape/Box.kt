package com.blakephillips.engine.utilities.shape

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.max
import kotlin.math.min

class Box(
    val p1: Vector2,
    val p2: Vector2
): Shape() {
    private val rectangle = Rectangle()
    override fun contains(pos: Vector2): Boolean {
        val maxX = max(p1.x, p2.x)
        val maxY = max(p1.y, p2.y)
        val minX = min(p1.x, p2.x)
        val minY = min(p1.y, p2.y)
        return (pos.x in minX..maxX) && (pos.y in minY..maxY)
    }

    override fun getHeight(): Float = p2.y - p1.y

    override fun getWidth(): Float = p2.x - p1.x

    override fun origin(): Vector2 = p1

    fun rectangle(): Rectangle =
        rectangle.also {
            it.x = origin().x
            it.y = origin().y
            it.height = getHeight()
            it.width = getWidth()
        }
}