package com.blakephillips.game.ui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.TextComponent;
import com.blakephillips.engine.ecs.components.TextureComponent;
import com.blakephillips.engine.ecs.components.mouse.FollowMouseComponent;
import com.blakephillips.engine.ecs.components.mouse.MousePositionComponent;
import com.blakephillips.engine.ecs.components.position.CenterPositionComponent;
import com.blakephillips.engine.ecs.components.position.DisplayPositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.components.position.SnapPositionComponent;

public class TileSelector {
    private Entity entity;

    public TileSelector(Engine engine) {
        TextureRegion mouseRegion = new TextureRegion(new Texture(Gdx.files.internal("tile_selector.png")));

        entity = new Entity();

        TextureComponent tex = new TextureComponent(mouseRegion, 1);
        entity.add(tex);
        entity.add(new PositionComponent(new Vector2(0, 0)));
        entity.add(new MousePositionComponent(new Vector2(0,0)));
        entity.add(new FollowMouseComponent());
        entity.add(new SnapPositionComponent(16, 16));
        entity.add(new CenterPositionComponent(mouseRegion.getRegionWidth(), mouseRegion.getRegionHeight()));
        entity.add(new TextComponent(""));
        entity.add(new DisplayPositionComponent());

        engine.addEntity(entity);
    }

}
