package com.blakephillips.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.PathComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.grid.Vertex;

public class PathFollowingSystem extends IntervalIteratingSystem {

    private ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<PathComponent> pathComponents = ComponentMapper.getFor(PathComponent.class);
    private TileMap tileMap;

    public PathFollowingSystem(TileMap tileMap) {
        super(Family.all(PositionComponent.class, PathComponent.class).get(), 0.1f);
        this.tileMap = tileMap;
    }

    @Override
    protected void processEntity(Entity entity) {
        PositionComponent pos = posComponents.get(entity);
        PathComponent path = pathComponents.get(entity);

        //If a path was added with just a destination but no path (wooo convenience)
        if (path.getPath() == null) {
            path.setPath(Pathfinding.getPath(pos.pos, path.getDestination(), tileMap));
        }

        //If just a path was added, grab last point for destination
        if (path.getDestination() == null) {
            path.setDestination(tileMap.cellIndexToWorld(path.getPath().getLast()));
        }

        if (path.getPath().isEmpty()) {
            entity.remove(PathComponent.class);
            return;
        }
        //for now tile by tile movement, can do this with velocity and not do a intervalSystem
        Vertex nextPos = path.getPath().remove(0);
        Vector2 v2pos = tileMap.cellIndexToWorld(nextPos);

        //Check if there wasn't an update to this edge since path creation
        if (!path.getPath().isEmpty() && !tileMap.graph.getGraph().edgeExists(nextPos, path.getPath().getFirst())) {
            //If we set the path to null, the path will be re-calculated and a new path will be found
            path.setPath(null);
        }

        pos.pos = v2pos;
    }

    public TileMap getTileMap() {
        return tileMap;
    }
}
