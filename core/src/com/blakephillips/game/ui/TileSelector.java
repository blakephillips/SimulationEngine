package com.blakephillips.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.gfx.TextComponent;
import com.blakephillips.engine.ecs.components.gfx.TextureComponent;
import com.blakephillips.engine.ecs.components.mouse.FollowMouseComponent;
import com.blakephillips.engine.ecs.components.mouse.MousePositionComponent;
import com.blakephillips.engine.ecs.components.position.CenterPositionComponent;
import com.blakephillips.engine.ecs.components.position.DisplayPositionComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.components.position.SnapPositionComponent;
import com.blakephillips.game.Orchestrator;

public class TileSelector {
    private final Entity entity;

    public TileSelector() {
        TextureRegion mouseRegion = new TextureRegion(new Texture(Gdx.files.internal("tile_selector.png")));

        entity = new Entity();
        entity.add(new TextureComponent(mouseRegion, 1));
        entity.add(new PositionComponent(new Vector2(0, 0)));
        entity.add(new MousePositionComponent(new Vector2(0,0)));
        entity.add(new FollowMouseComponent());
        entity.add(new SnapPositionComponent(16, 16));
        entity.add(new CenterPositionComponent(mouseRegion.getRegionWidth(), mouseRegion.getRegionHeight()));
        entity.add(new TextComponent(""));
        entity.add(new DisplayPositionComponent());

        Orchestrator.engine.addEntity(entity);
    }

}
