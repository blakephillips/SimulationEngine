package com.blakephillips.game.ui

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.components.ai.JobComponent
import com.blakephillips.engine.ecs.components.gfx.ShapeComponent
import com.blakephillips.engine.ecs.components.gfx.TextureComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.engine.utilities.shape.Box
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.ai.states.DeferrableHarvestState
import com.blakephillips.game.data.JobStatus
import com.blakephillips.game.data.JobType
import com.blakephillips.game.data.ObjectType
import com.blakephillips.game.ecs.components.HarvestableComponent
import com.blakephillips.game.ecs.components.JobTypeComponent
import com.blakephillips.game.ecs.components.ObjectTypeComponent
import com.blakephillips.game.ecs.components.SelectableComponent

class BoxSelection(private val startPos: Vector2, val selectionType: ObjectType?) {
    private val endPos = Vector2(startPos.x, startPos.y)
    val selectedEntities: HashMap<Entity, Entity> = hashMapOf()
    val box = Box(startPos, endPos)
    var shapeComponent: ShapeComponent? = null
    val entity: Entity = Entity()
    var selectedTileRegion = TextureRegion(Texture(Gdx.files.internal("tile_selector.png")))

    fun setStartPosition(x: Float, y: Float) {
        startPos.x = x
        startPos.y = y
    }

    fun setEndPosition(x: Float, y: Float) {
        endPos.x = x
        endPos.y = y

        val selectable = getSelectableEntities()
        clear()
        for (entity in selectable) {
            val selectableComponent = selectableComponents[entity]
            val posComponent = positionComponents.get(entity)
            selectableComponent.selected = box.contains(posComponent.pos)
            if (selectableComponent.selected && !selectedEntities.containsKey(entity)) {

                // If selection type is provided, skip any objects that don't match the selectionType
                if (selectionType != null) {
                    if (!objectTypeComponents.has(entity) || objectTypeComponents.get(entity).objectType !== selectionType) continue
                }

                val selectedEntity = Entity().also {
                    it.add(TextureComponent(selectedTileRegion, 1))
                    it.add(posComponent)
                    selectableComponent.selectionEntity = it
                }
                selectedEntities[entity] = selectedEntity
                Orchestrator.engine.addEntity(selectedEntity)

                val harvestable = harvestableComponents[entity]
                if (harvestable != null && !harvestable.harvestQueued) {
                    val harvestJobEntity = Entity().also {
                        val harvestState = DeferrableHarvestState(entity)
                        it.add(JobComponent("Harvest Tree", JobStatus.IDLE, harvestState))
                        it.add(JobTypeComponent(JobType.CUT))
                    }
                    harvestable.harvestQueued = true
                    Orchestrator.engine.addEntity(harvestJobEntity)
                }
            }
        }
    }

    fun clear() {
        for (entityPair in selectedEntities) {
            selectableComponents[entityPair.key].selected = false
            Orchestrator.engine.removeEntity(entityPair.value)
        }
        selectedEntities.clear()
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
        Orchestrator.engine.getEntitiesFor(
            Family.all(
                SelectableComponent::class.java,
                PositionComponent::class.java
            ).get()
        )

    companion object {
        private val selectableComponents = ComponentMapper.getFor(SelectableComponent::class.java)
        private val positionComponents = ComponentMapper.getFor(PositionComponent::class.java)
        private val harvestableComponents = ComponentMapper.getFor(HarvestableComponent::class.java)
        private val objectTypeComponents = ComponentMapper.getFor(ObjectTypeComponent::class.java)
    }

}