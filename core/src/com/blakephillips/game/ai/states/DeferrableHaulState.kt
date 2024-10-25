package com.blakephillips.game.ai.states

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ai.DeferrableState
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.ResourceType
import com.blakephillips.game.ecs.systems.ResourceSystem

class DeferrableHaulState(val resourceType: ResourceType, val destination: Vector2) : DeferrableState(null) {

    override fun update(deltaTime: Float) {
        exit()
    }

    override fun gatherRequirements() {
        val resourceSystem = Orchestrator.engine.getSystem(ResourceSystem::class.java)
        val pos: PositionComponent = positionComponents[entity]


        val resource = resourceSystem.getClosestReachableResourceOfType(resourceType, pos)

        if (resource == null) {
            Gdx.app.log(
                this::class.simpleName,
                "GetResourceTypeToDestination Cannot find resource of type $resourceType"
            )
            exit(true)
            return
        }

        nextState = HaulState(entity, resource, destination)
    }

    companion object {
        private val positionComponents = ComponentMapper.getFor(PositionComponent::class.java)
    }
}