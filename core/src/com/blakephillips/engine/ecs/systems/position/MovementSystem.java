package com.blakephillips.engine.ecs.systems.position;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.components.position.VelocityComponent;

public class MovementSystem extends IteratingSystem {
    private final ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> velComponents = ComponentMapper.getFor(VelocityComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = posComponents.get(entity);
        VelocityComponent vel = velComponents.get(entity);
        pos.pos.x += vel.dx * deltaTime;
        pos.pos.y += vel.dy * deltaTime;
    }
}
