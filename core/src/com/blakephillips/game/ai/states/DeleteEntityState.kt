package com.blakephillips.game.ai.states

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.blakephillips.engine.ai.State
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.ecs.components.DropItemComponent
import com.blakephillips.game.ecs.components.SelectableComponent

class DeleteEntityState(val entityToDelete: Entity) : State(null) {
    override fun enter() {
        val engine = Orchestrator.engine
        val dropItem = dropItemComponents[entityToDelete]
        val positionComponent = positionComponents[entityToDelete]
        val selectableComponent = selectableComponents[entityToDelete]
        if (dropItem !== null && positionComponent !== null) {
            dropItem.entity.add(PositionComponent(positionComponent.pos))
            Orchestrator.engine.addEntity(dropItem.entity)
            if (selectableComponent.selectionEntity !== null) engine.removeEntity(selectableComponent.selectionEntity)
        }
        engine.removeEntity(entityToDelete)
        exit()
    }

    override fun update(deltaTime: Float) {
        exit()
    }

    companion object {
        val dropItemComponents = ComponentMapper.getFor(DropItemComponent::class.java)
        val positionComponents = ComponentMapper.getFor(PositionComponent::class.java)
        val selectableComponents = ComponentMapper.getFor(SelectableComponent::class.java)
    }
}