package com.blakephillips.engine.ai;

import com.badlogic.ashley.core.Entity;

public abstract class State {
    public Entity entity;

    public State(Entity entity) {
        this.entity = entity;
    }

    abstract void enter();
    abstract void exit();
    abstract void update(float deltaTime);
}
