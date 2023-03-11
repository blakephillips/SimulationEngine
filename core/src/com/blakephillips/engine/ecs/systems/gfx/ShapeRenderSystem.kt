package com.blakephillips.engine.ecs.systems.gfx

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.blakephillips.engine.ecs.components.gfx.ShapeComponent
import com.blakephillips.engine.utilities.shape.Box
import space.earlygrey.shapedrawer.ShapeDrawer
import kotlin.math.sign

class ShapeRenderSystem(private val batch: SpriteBatch): SortedIteratingSystem(
    Family.all(ShapeComponent::class.java).get(),
    ZComparator
) {
    private val shapeDrawer = ShapeDrawer(batch, region)
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val shapeComponent = shapeComponents[entity]

        when (shapeComponent.shape) {
            is Box -> shapeDrawer.rectangle(shapeComponent.shape.rectangle(), shapeComponent.color)
        }
    }


    private object ZComparator: Comparator<Entity> {
        override fun compare(o1: Entity?, o2: Entity?): Int {
            return sign((shapeComponents[o1].z - shapeComponents[o2].z).toFloat()).toInt()
        }
    }
    companion object {
        private val shapeComponents = ComponentMapper.getFor(ShapeComponent::class.java)
        private val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).also {
            it.setColor(Color.WHITE)
            it.drawPixel(0,0)
        }
        private val region = TextureRegion(Texture(pixmap), 0, 0, 1, 1)
    }
}