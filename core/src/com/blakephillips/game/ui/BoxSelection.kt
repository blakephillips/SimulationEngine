package com.blakephillips.game.ui

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.components.gfx.ShapeComponent
import com.blakephillips.engine.utilities.shape.Box
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.ObjectType
import com.blakephillips.game.ecs.components.SelectableComponent

class BoxSelection(private val startPos: Vector2, val selectionType: ObjectType?) {
    private val endPos = Vector2(startPos.x, startPos.y)
    val selectedEntities: HashSet<Entity> = hashSetOf()
    val box = Box(startPos, endPos)
    var shapeComponent: ShapeComponent? = null
    val entity: Entity = Entity()

    fun setStartPosition(x: Float, y: Float) {
        startPos.x = x
        startPos.y = y
    }

    fun setEndPosition(x: Float, y: Float) {
        endPos.x = x
        endPos.y = y

        val selectable = getSelectableEntities()

        for (entity in selectable) {
            val selectableComponent = selectableComponents[entity]
            val pos = selectableComponent.pos.pos
            selectableComponent.selected = box.contains(pos)
        }
    }

    fun visible() {
        if (shapeComponent != null) return

        shapeComponent = ShapeComponent(box)

        Orchestrator.engine.addEntity(
            entity.also { it.add(shapeComponent) }
        )
    }

    fun visible(x: Float, y: Float) {
        setEndPosition(x, y)
        visible()
    }

    fun remove() {
        if (shapeComponent == null) return

        Orchestrator.engine.removeEntity(entity)
        entity.remove(ShapeComponent::class.java)
        shapeComponent = null
    }

    private fun getSelectableEntities() =
        Orchestrator.engine.getEntitiesFor(Family.all(SelectableComponent::class.java).get())

    companion object {
        private var selectableComponents = ComponentMapper.getFor(SelectableComponent::class.java)
    }

}