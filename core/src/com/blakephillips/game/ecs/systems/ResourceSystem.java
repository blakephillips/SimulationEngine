package com.blakephillips.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakephillips.game.data.ResourceType;
import com.blakephillips.game.ecs.components.ResourceComponent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResourceSystem extends IteratingSystem {

    private ComponentMapper<ResourceComponent> resourceComponents = ComponentMapper.getFor(ResourceComponent.class);
    private Map<ResourceType, Set<Entity>> resources = new HashMap<>();
    public ResourceSystem() {
        super(Family.all(ResourceComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        ResourceComponent resourceComponent = resourceComponents.get(entity);

        if (!resources.containsKey(resourceComponent.getType())) {
            resources.put(resourceComponent.getType(), new HashSet<>());
        }

        if (!resources.get(resourceComponent.getType()).contains(entity)) {
            resources.get(resourceComponent.getType()).add(entity);
        }
    }

    public Set<Entity> getEntitiesOfType(ResourceType resourceType) {
        if (!resources.containsKey(resourceType)) { return null; }
        return resources.get(resourceType);
    }

}
