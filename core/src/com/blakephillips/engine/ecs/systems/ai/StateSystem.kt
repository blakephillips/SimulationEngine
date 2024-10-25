package com.blakephillips.engine.ecs.systems.ai

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.blakephillips.engine.ai.State.StateStatus
import com.blakephillips.engine.ecs.components.ai.StateComponent

class StateSystem : IteratingSystem(Family.all(StateComponent::class.java).get()) {
    private var stateComponents: ComponentMapper<StateComponent> = ComponentMapper.getFor(StateComponent::class.java)
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val stateComponent = stateComponents.get(entity)

        when (stateComponent.state.stateStatus) {
            StateStatus.IDLE -> stateComponent.state.enter()
            StateStatus.RUNNING -> stateComponent.state.update(deltaTime)
            StateStatus.COMPLETE -> entity.remove(StateComponent::class.java)
            else -> {}
        }
    }
}