package com.blakephillips.engine.ecs.systems.mouse;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakephillips.engine.ecs.components.mouse.FollowMouseComponent;
import com.blakephillips.engine.ecs.components.mouse.MousePositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;

public class FollowMousePositionSystem extends IteratingSystem {

    private final ComponentMapper<MousePositionComponent> mouseComponents = ComponentMapper.getFor(MousePositionComponent.class);
    private final ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);

    public FollowMousePositionSystem() {
        super(Family.all(MousePositionComponent.class, FollowMouseComponent.class, PositionComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MousePositionComponent mousePos = mouseComponents.get(entity);
        PositionComponent pos = posComponents.get(entity);
        pos.pos.x = mousePos.pos.x;
        pos.pos.y = mousePos.pos.y;
    }
}
