package com.blakephillips.engine.ecs.systems.gfx;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.blakephillips.engine.ecs.components.gfx.TextureComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    private SpriteBatch batch;
    private ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> texComponents = ComponentMapper.getFor(TextureComponent.class);

    public RenderSystem(SpriteBatch batch) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), new ZComparator());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = posComponents.get(entity);
        TextureComponent tex = texComponents.get(entity);

        batch.draw(tex.region, pos.pos.x, pos.pos.y);
    }

    private static class ZComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity entityA, Entity entityB) {
            ComponentMapper<TextureComponent> texComponents = ComponentMapper.getFor(TextureComponent.class);
            return (int)Math.signum(texComponents.get(entityA).z - texComponents.get(entityB).z);
        }
    }
}


