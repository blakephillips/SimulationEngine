package com.blakephillips.game.ai.states

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ai.State
import com.blakephillips.engine.ecs.components.PathComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.engine.ecs.systems.PathFollowingSystem
import com.blakephillips.engine.utilities.grid.Pathfinding
import com.blakephillips.game.Orchestrator

class PathFindingState(actor: Entity?, private var destination: Vector2) : State(actor) {
    override fun enter() {
        Gdx.app.log("Game", "Path finding state entered")
        if (!posComponents.has(entity)) {
            Gdx.app.error("Game", "Attempted pathing entity has no position component.")
            exit(true)
            return
        }
        val positionComponent = posComponents[entity]
        val tileMap = Orchestrator.engine.getSystem(PathFollowingSystem::class.java).tileMap
        val vertexPath = Pathfinding.getPath(positionComponent.pos, destination, tileMap)
        val pathComponent = PathComponent(vertexPath)
        entity.add(pathComponent)
        setStateStatus(StateStatus.RUNNING)
    }

    override fun exit() {
        entity.remove(PathComponent::class.java)
        setStateStatus(StateStatus.COMPLETE)
        Gdx.app.log("Game", "Exited path finding state")
    }

    override fun update(deltaTime: Float) {
        val positionComponent = posComponents[entity]
        if (stateStatus == StateStatus.RUNNING && !pathComponents.has(entity)) {
            if (Pathfinding.chebyshevDistance(positionComponent!!.pos, destination) > PROXIMITY) {
                exit(true)
                return
            }
            exit()
        }
    }

    fun setDestination(destination: Vector2) {
        this.destination = destination
    }

    private companion object {
        const val PROXIMITY = 15

        val posComponents: ComponentMapper<PositionComponent> = ComponentMapper.getFor(
            PositionComponent::class.java
        )
        val pathComponents: ComponentMapper<PathComponent> = ComponentMapper.getFor(
            PathComponent::class.java
        )
    }
}
