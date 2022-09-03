package com.blakephillips.engine.ecs.systems.gfx;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.blakephillips.engine.ecs.components.TextComponent;
import com.blakephillips.engine.ecs.components.position.DisplayPositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;

public class TextRenderSystem extends IteratingSystem {

    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch;

    private ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextComponent> textComponents = ComponentMapper.getFor(TextComponent.class);
    private ComponentMapper<DisplayPositionComponent> displayPosComponents = ComponentMapper.getFor(DisplayPositionComponent.class);
    public TextRenderSystem(SpriteBatch batch) {
        super(Family.all(TextComponent.class, PositionComponent.class).get());
        this.batch = batch;

        font.getData().setScale(0.55f, 0.55f);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = posComponents.get(entity);
        TextComponent text = textComponents.get(entity);

        if (displayPosComponents.has(entity)) {
            text.content = String.format("(%s, %s)", Float.toString(pos.pos.x), Float.toString(pos.pos.y));
        }

        font.draw(batch, text.content, pos.pos.x, pos.pos.y);
    }
}
