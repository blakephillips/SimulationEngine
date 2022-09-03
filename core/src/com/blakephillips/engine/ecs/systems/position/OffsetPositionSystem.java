package com.blakephillips.engine.ecs.systems.position;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakephillips.engine.ecs.components.position.OffsetPositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;

public class OffsetPositionSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<OffsetPositionComponent> offsetComponents = ComponentMapper.getFor(OffsetPositionComponent.class);

    public OffsetPositionSystem() {
        super(Family.all(PositionComponent.class, OffsetPositionComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        PositionComponent pos = posComponents.get(entity);
        OffsetPositionComponent offset = offsetComponents.get(entity);

        if (!offset.lastPosition.equals(pos.pos)) {
            pos.pos.x += offset.offsetX;
            pos.pos.y += offset.offsetY;

            offset.lastPosition.x = pos.pos.x;
            offset.lastPosition.y = pos.pos.y;
        }

    }


}
