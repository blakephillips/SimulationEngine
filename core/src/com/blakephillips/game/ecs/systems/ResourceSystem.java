package com.blakephillips.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.blakephillips.engine.ecs.components.ai.ReservedComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.systems.PathFollowingSystem;
import com.blakephillips.engine.ecs.systems.gfx.TilemapRenderSystem;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.game.Orchestrator;
import com.blakephillips.game.data.ResourceType;
import com.blakephillips.game.ecs.components.ResourceComponent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResourceSystem extends IteratingSystem {

    private final ComponentMapper<ResourceComponent> resourceComponents = ComponentMapper.getFor(ResourceComponent.class);
    private final ComponentMapper<ReservedComponent> reservedComponents = ComponentMapper.getFor(ReservedComponent.class);
    private final ComponentMapper<PositionComponent> positionComponents = ComponentMapper.getFor(PositionComponent.class);
    private final Map<ResourceType, Set<Entity>> resources = new HashMap<>();
    public ResourceSystem() {
        super(Family.all(ResourceComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        ResourceComponent resourceComponent = resourceComponents.get(entity);

        if (!resources.containsKey(resourceComponent.getType())) {
            resources.put(resourceComponent.getType(), new HashSet<>());
        }

        resources.get(resourceComponent.getType()).add(entity);
    }

    public Set<Entity> getAllResourcesOfType(ResourceType resourceType) {
        if (!resources.containsKey(resourceType)) { return null; }
        return resources.get(resourceType);
    }

    public Entity getClosestReachableResourceOfType(ResourceType resourceType, PositionComponent positionComponent) {
        Set<Entity> entities = this.getAllResourcesOfType(resourceType);
        Entity closestEntity = null;
         Engine engine = Orchestrator.getEngine();
        if (entities == null || entities.isEmpty()) { return null; }
        for (Entity entity: entities) {
            if (reservedComponents.has(entity)) {
                continue;
            }

            if (!positionComponents.has(entity)) {
                Gdx.app.error("SimEngine", "Resource Entity does not have a position. " +
                        "Cannot use this for hauling.");
                continue;
            }

            PositionComponent resourcePosition = positionComponents.get(entity);
            TileMap tileMap = engine.getSystem(PathFollowingSystem.class).getTileMap();

            if (closestEntity == null && Pathfinding.isReachable(
                    positionComponent.pos,
                    resourcePosition.pos,
                    tileMap
            )) {
                closestEntity = entity;
            } else if (closestEntity != null) {
                PositionComponent closestResourceEntityPosition = positionComponents.get(closestEntity);
                // is this new entity closer to the requesting entity?
                if (Math.abs(Pathfinding.chebyshevDistance(positionComponent.pos, resourcePosition.pos)) <
                        Math.abs(Pathfinding.chebyshevDistance(positionComponent.pos, closestResourceEntityPosition.pos))) {
                    // if so, can we get there?
                    if (Pathfinding.isReachable(positionComponent.pos, resourcePosition.pos, tileMap)) {
                        // NOTE: this will pick the closest entity (straight line), but no regard with path length.
                        // this can be added in the future, if needed.
                        closestEntity = entity;
                    }
                }
            }
        }

        return closestEntity;
    }

}
