package com.blakephillips.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.TextureComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;

public class Character {
    Entity entity;
    public Character(Vector2 pos, Engine engine) {
        entity = new Entity();
        SpriteSheet characterSpriteSheet = new SpriteSheet("capybara.png", 32, 32);
        TextureRegion spr = characterSpriteSheet.getTextureFromTileMap(0,0);
        entity.add(new PositionComponent(pos));
        entity.add(new TextureComponent(spr, 0));

        engine.addEntity(entity);
    }

}
