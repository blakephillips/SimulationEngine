package com.blakephillips.game.ai.states

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.blakephillips.engine.ai.DeferrableState
import com.blakephillips.engine.ecs.components.ai.JobComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.JobStatus
import com.blakephillips.game.data.JobType
import com.blakephillips.game.data.ResourceType
import com.blakephillips.game.ecs.components.HarvestableComponent
import com.blakephillips.game.ecs.components.JobTypeComponent

class DeferrableHarvestState(val entityToHarvest: Entity) : DeferrableState(null) {
    private companion object {
        val harvestableComponents = ComponentMapper.getFor(HarvestableComponent::class.java)
        val positionComponents = ComponentMapper.getFor(PositionComponent::class.java)
    }

    override fun gatherRequirements() {
        val position = positionComponents[entityToHarvest]
        val harvestable = harvestableComponents[entityToHarvest]

        if (position == null) {
            Gdx.app.error(this::class.java.simpleName, "entityToHarvest has no position component")
            exit(true)
            return
        }

        if (harvestable == null) {
            Gdx.app.error(this::class.java.simpleName, "entityToHarvest has no harvestable component")
            exit(true)
            return
        }

        val haulState = DeferrableHaulState(ResourceType.AXE, position.pos)
        haulState.setStateForAfterDeferred(DeleteEntityState(entityToHarvest))
        val entity = Entity().also {
            it.add(JobComponent("Harvest DeferrableHaulState", JobStatus.IDLE, haulState))
            it.add(JobTypeComponent(JobType.CUT))
        }
        Orchestrator.engine.addEntity(entity)
    }

    override fun update(deltaTime: Float) {
        exit()
    }
}