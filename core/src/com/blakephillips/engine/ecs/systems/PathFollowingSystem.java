package com.blakephillips.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.PathComponent;
import com.blakephillips.engine.ecs.components.position.DirectionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.utilities.Direction;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.grid.Vertex;
import space.earlygrey.simplegraphs.Path;

public class PathFollowingSystem extends IntervalIteratingSystem {

    private final ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<PathComponent> pathComponents = ComponentMapper.getFor(PathComponent.class);
    private final ComponentMapper<DirectionComponent> directionComponents = ComponentMapper.getFor(DirectionComponent.class);
    private final TileMap tileMap;

    public PathFollowingSystem(TileMap tileMap) {
        super(Family.all(PositionComponent.class, PathComponent.class).get(), 0.1f);
        this.tileMap = tileMap;
    }

    @Override
    protected void processEntity(Entity entity) {
        PositionComponent pos = posComponents.get(entity);
        PathComponent path = pathComponents.get(entity);

        //There is no path component (just got removed)
        if (path == null) {
            //idk just in case
            entity.remove(PathComponent.class);
            return;
        }

        //If a path was added with just a destination but no path (wooo convenience)
        if (path.getPath() == null) {
            path.setPath(Pathfinding.getPath(pos.pos, path.getDestination(), tileMap));
        }

        //If just a path was added, grab last point for destination
        if (path.getDestination() == null && !path.getPath().isEmpty()) {
            path.setDestination(tileMap.cellIndexToWorld(path.getPath().getLast()));
        }

        if (path.getPath().isEmpty()) {
            entity.remove(PathComponent.class);
            return;
        }
        //for now tile by tile movement, can do this with velocity and not do a intervalSystem
        Vertex nextPos = path.getPath().remove(0);
        Vector2 v2pos = tileMap.cellIndexToWorld(nextPos);

        for (int i = 0; i < path.getPath().getLength() - 1; i++) {
            if (i > 5) {
                break;
            }
            Path<Vertex> vertexPath = path.getPath();
            if (!tileMap.graph.getGraph().edgeExists(vertexPath.get(i), vertexPath.get(i + 1))) {

                //when a path is null, but there is a destination,
                //will attempt to re-path to the destination
                path.setPath(null);
                break;
            }
        }

        //Set direction when pathing if entity has a direction component
        //This responsibility can be moved elsewhere in the future
        if (directionComponents.has(entity)) {
            DirectionComponent directionComponent = directionComponents.get(entity);
            if (pos.pos.y < v2pos.y) {
                directionComponent.setDirection(Direction.NORTH);
            } else if (pos.pos.y > v2pos.y) {
                directionComponent.setDirection(Direction.SOUTH);
            } else if (pos.pos.x < v2pos.x) {
                directionComponent.setDirection(Direction.EAST);
            } else if (pos.pos.x > v2pos.x) {
                directionComponent.setDirection(Direction.WEST);
            }
        }

        pos.pos = v2pos;
    }

    public TileMap getTileMap() {
        return tileMap;
    }
}
