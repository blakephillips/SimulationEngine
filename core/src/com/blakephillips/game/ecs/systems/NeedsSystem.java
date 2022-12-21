package com.blakephillips.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.blakephillips.game.ecs.components.HungerComponent;

public class NeedsSystem extends IntervalIteratingSystem {

    private final ComponentMapper<HungerComponent> hungerComponents = ComponentMapper.getFor(HungerComponent.class);
    public NeedsSystem() {
        super(Family.one(HungerComponent.class).get(), 1f);
    }

    @Override
    protected void processEntity(Entity entity) {

    }
}
