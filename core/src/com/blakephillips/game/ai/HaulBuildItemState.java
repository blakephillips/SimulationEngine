package com.blakephillips.game.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.blakephillips.engine.ai.State;
import com.blakephillips.game.ecs.components.BuildObjectComponent;

public class HaulBuildItemState extends State {

    private BuildObjectComponent buildObjectComponent;
    private ComponentMapper<BuildObjectComponent> buildObjectComponents = ComponentMapper.getFor(BuildObjectComponent.class);
    public HaulBuildItemState(Entity entity, Entity buildObject) {
        super(entity);
        if (!buildObjectComponents.has(buildObject)) {
            Gdx.app.error("Game", "Build item object has no BuildObjectComponent");
            exit(true);
            return;
        }
        buildObjectComponent = buildObjectComponents.get(buildObject);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void update(float deltaTime) {

    }
}
