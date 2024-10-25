package com.blakephillips.engine.ecs.components.gfx

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.blakephillips.engine.utilities.shape.Shape

class ShapeComponent(
    val shape: Shape,
    val shapeType: ShapeRenderer.ShapeType = ShapeRenderer.ShapeType.Filled,
    val color: Color = Color.GRAY,
    val z: Int = 0
) : Component