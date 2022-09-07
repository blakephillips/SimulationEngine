package com.blakephillips.engine.ecs.systems.position;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.components.position.SnapPositionComponent;
import com.blakephillips.engine.ecs.components.position.SnapToPositionComponent;

public class SnapPositionSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<SnapPositionComponent> snapComponents = ComponentMapper.getFor(SnapPositionComponent.class);
    private ComponentMapper<SnapToPositionComponent> snapToComponents = ComponentMapper.getFor(SnapToPositionComponent.class);

    public SnapPositionSystem() {
        super(Family.all(PositionComponent.class).one(SnapPositionComponent.class, SnapToPositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = posComponents.get(entity);

        if (snapToComponents.has(entity)) {
            SnapToPositionComponent snapTo = snapToComponents.get(entity);
            pos.pos.x = snapTo.posComponent.pos.x;
            pos.pos.y = snapTo.posComponent.pos.y;
        }

        if (snapComponents.has(entity)) {
            SnapPositionComponent snap = snapComponents.get(entity);
            pos.pos.x = MathUtils.round(pos.pos.x / snap.snapX) * snap.snapX;
            pos.pos.y = MathUtils.round(pos.pos.y / snap.snapY) * snap.snapY;
        }

    }
}
