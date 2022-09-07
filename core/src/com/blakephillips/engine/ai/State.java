package com.blakephillips.engine.ai;

import com.badlogic.ashley.core.Entity;

public abstract class State {
    public Entity entity;
    public StateStatus stateStatus = StateStatus.IDLE;
    public State(Entity entity) {
        this.entity = entity;
    }

    public abstract void enter();
    public abstract void exit();
    public abstract void update(float deltaTime);

    public enum StateStatus {
        IDLE,
        RUNNING,
        EXITED
    }

}
