package com.blakephillips.engine.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.blakephillips.engine.ecs.components.PathComponent
import com.blakephillips.engine.ecs.components.position.DirectionComponent
import com.blakephillips.engine.ecs.components.position.PositionComponent
import com.blakephillips.engine.utilities.Direction
import com.blakephillips.engine.utilities.component.ComponentUtility.getFor
import com.blakephillips.engine.utilities.grid.Pathfinding
import com.blakephillips.engine.utilities.grid.TileMap

class PathFollowingSystem(val tileMap: TileMap) :
    IntervalIteratingSystem(Family.all(PositionComponent::class.java, PathComponent::class.java).get(), 0.1f) {
    override fun processEntity(entity: Entity) {
        val positionComponent = posComponents[entity]
        val pathComponent = pathComponents[entity]

        if (pathComponent.path.isEmpty()) {
            entity.remove(PathComponent::class.java)
            return
        }

        //for now tile by tile movement, can do this with velocity and not do a intervalSystem
        val currentPos = tileMap.worldToCellIndex(positionComponent.pos)
        var nextPos = pathComponent.path.get(0)



        if (currentPos != nextPos && !tileMap.graph.getGraph().edgeExists(currentPos, nextPos)) {
            val destination = pathComponent.path.last
            pathComponent.path.clear()
            val newPath = Pathfinding.getPath(currentPos, destination, tileMap.graph.getGraph())
            if (newPath.isEmpty()) {
                Gdx.app.log("PathFollowingSystem", "Can't find a new path. Removing path component")
                entity.remove(PathComponent::class.java)
                return
            }
            pathComponent.path.addAll(newPath)
            nextPos = pathComponent.path.get(0)
        }
        pathComponent.path.remove(0)
        val v2pos = tileMap.cellIndexToWorld(nextPos)


        //Set direction when pathing if entity has a direction component
        //This responsibility can be moved elsewhere in the future
        if (directionComponents.has(entity)) {
            val directionComponent = directionComponents[entity]
            if (positionComponent.pos.y < v2pos.y) {
                directionComponent.direction = Direction.NORTH
            } else if (positionComponent.pos.y > v2pos.y) {
                directionComponent.direction = Direction.SOUTH
            } else if (positionComponent.pos.x < v2pos.x) {
                directionComponent.direction = Direction.EAST
            } else if (positionComponent.pos.x > v2pos.x) {
                directionComponent.direction = Direction.WEST
            }
        }
        positionComponent.pos = v2pos
    }
    private companion object {
        private val posComponents = PositionComponent::class.getFor()
        private val pathComponents = PathComponent::class.getFor()
        private val directionComponents = DirectionComponent::class.getFor()
    }
}
