package com.blakephillips.engine.ecs.systems.position;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakephillips.engine.ecs.components.position.CenterPositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;

public class CenterPositionSystem extends IteratingSystem {

    private final ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<CenterPositionComponent> centerPosComponents = ComponentMapper.getFor(CenterPositionComponent.class);

    public CenterPositionSystem() {
        super(Family.all(CenterPositionComponent.class, PositionComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        CenterPositionComponent centerPos = centerPosComponents.get(entity);
        PositionComponent pos = posComponents.get(entity);

        if (!centerPos.lastPosition.equals(pos.pos)) {

            pos.pos.x = pos.pos.x - (centerPos.width / 2);
            pos.pos.y = pos.pos.y - (centerPos.height / 2);

            centerPos.lastPosition.x = pos.pos.x;
            centerPos.lastPosition.y = pos.pos.y;
        }
    }

}
