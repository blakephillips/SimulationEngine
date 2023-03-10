package com.blakephillips.game.ui

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.components.ai.JobComponent
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.ObjectType
import com.blakephillips.game.ecs.components.SelectableComponent
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min

class BoxSelection(private val startPos: Vector2, val selectionType: ObjectType?) {
    private val endPos = Vector2(startPos.x, startPos.y)
    val selectedEntities: HashSet<Entity> = hashSetOf()

    fun setEndPosition(x: Float, y: Float) {
        endPos.x = x
        endPos.y = y

        val max_x = max(startPos.x, endPos.x)
        val max_y = max(startPos.y, endPos.y)
        val min_x = min(startPos.x, endPos.x)
        val min_y = min(startPos.y, endPos.y)

        val selectable = getSelectableEntities()

        for (entity in selectable) {
            val selectableComponent = selectableComponents[entity]
            val pos = selectableComponent.pos.pos
            selectableComponent.selected = (pos.x in min_x..max_x) && (pos.y in min_y..max_y)
        }
    }

    private fun getSelectableEntities() =
        Orchestrator.getEngine().getEntitiesFor(Family.all(SelectableComponent::class.java).get())

    companion object {
        private var selectableComponents = ComponentMapper.getFor(SelectableComponent::class.java)
    }

}