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
        if (path.path == null) {
            path.path = Pathfinding.getPath(pos.pos, path.destination, tileMap);
        }

        if (path.path.isEmpty()) {
            entity.remove(PathComponent.class);
            return;
        }

        //for now tile by tile movement, can do this with velocity and not do a intervalSystem
        Vertex nextPos = path.path.remove(0);
        Vector2 v2pos = tileMap.cellIndexToWorld(nextPos);

        pos.pos = v2pos;
    }

    public TileMap getTileMap() {
        return tileMap;
    }
}
