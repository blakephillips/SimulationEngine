package com.blakephillips.engine.ecs.systems.position;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakephillips.engine.ecs.components.TextureComponent;
import com.blakephillips.engine.ecs.components.position.CenterPositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;

public class CenterPositionSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<CenterPositionComponent> centerPosComponents = ComponentMapper.getFor(CenterPositionComponent.class);
    private ComponentMapper<TextureComponent> texComponents = ComponentMapper.getFor(TextureComponent.class);

    //Only centers entities with a texture to get size from. This could be tweaked in the future.
    public CenterPositionSystem() {
        super(Family.all(CenterPositionComponent.class, PositionComponent.class, TextureComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        TextureComponent tex = texComponents.get(entity);
        CenterPositionComponent centerPos = centerPosComponents.get(entity);
        PositionComponent pos = posComponents.get(entity);

        if (!centerPos.lastPosition.equals(pos.pos)) {

            pos.pos.x = pos.pos.x - (tex.region.getRegionWidth() / 2);
            pos.pos.y = pos.pos.y - (tex.region.getRegionHeight() / 2);

            centerPos.lastPosition.x = pos.pos.x;
            centerPos.lastPosition.y = pos.pos.y;
        }
    }

}
