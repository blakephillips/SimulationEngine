package com.blakephillips.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.ai.CurrentJobComponent;
import com.blakephillips.engine.ecs.components.gfx.DirectionalTextureComponent;
import com.blakephillips.engine.ecs.components.gfx.TextureComponent;
import com.blakephillips.engine.ecs.components.position.DirectionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.utilities.Direction;
import com.blakephillips.engine.utilities.sprite.SpriteSheet;
import com.blakephillips.game.data.ObjectType;
import com.blakephillips.game.ecs.components.ObjectTypeComponent;
import com.blakephillips.game.ecs.components.SelectableComponent;

public class Character {
    Entity entity;

    public Character(Vector2 pos, Engine engine) {
        entity = new Entity();
        SpriteSheet characterSpriteSheet = new SpriteSheet("capybara.png", 32, 32);
        TextureRegion spr_west = characterSpriteSheet.getTextureFromTileMap(0, 0);
        TextureRegion spr_east = characterSpriteSheet.getTextureFromTileMap(0, 0, true);
        TextureRegion spr_north = characterSpriteSheet.getTextureFromTileMap(0, 2);
        TextureRegion spr_south = characterSpriteSheet.getTextureFromTileMap(0, 1);

        entity.add(new PositionComponent(pos));
        entity.add(new TextureComponent(spr_west, 0));
        entity.add(new DirectionalTextureComponent(spr_north, spr_east, spr_south, spr_west));
        entity.add(new DirectionComponent(Direction.WEST));
        entity.add(new CurrentJobComponent(null));
        entity.add(new ObjectTypeComponent(ObjectType.CHARACTER));
        entity.add(new SelectableComponent());

        engine.addEntity(entity);
    }

}
