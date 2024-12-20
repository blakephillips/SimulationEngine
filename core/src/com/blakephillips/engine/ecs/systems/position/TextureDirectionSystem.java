package com.blakephillips.engine.ecs.systems.position;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.blakephillips.engine.ecs.components.gfx.DirectionalTextureComponent;
import com.blakephillips.engine.ecs.components.gfx.TextureComponent;
import com.blakephillips.engine.ecs.components.position.DirectionComponent;

public class TextureDirectionSystem extends IteratingSystem {

    private final ComponentMapper<TextureComponent> textureComponents = ComponentMapper.getFor(TextureComponent.class);
    private final ComponentMapper<DirectionComponent> directionComponents = ComponentMapper.getFor(DirectionComponent.class);
    private final ComponentMapper<DirectionalTextureComponent> directionalTexComponents = ComponentMapper.getFor(DirectionalTextureComponent.class);

    public TextureDirectionSystem() {
        super(Family.all(TextureComponent.class, DirectionComponent.class, DirectionalTextureComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent textureComponent = textureComponents.get(entity);
        DirectionComponent directionComponent = directionComponents.get(entity);
        DirectionalTextureComponent directionalTextureComponent = directionalTexComponents.get(entity);

        textureComponent.setRegion(directionalTextureComponent.getTexture(directionComponent.direction));
    }
}
