package com.blakephillips.engine.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.blakephillips.engine.ecs.components.ai.ReservedComponent;

public abstract class State {
    public Entity entity;
    public StateStatus stateStatus = StateStatus.IDLE;
    public State(Entity entity) {
        this.entity = entity;
    }

    public abstract void enter();
    public abstract void exit();
    public abstract void update(float deltaTime);
    public void reserveEntity(Entity entityToReserve) {
        entityToReserve.add(new ReservedComponent(entity));
    }

    //Check if this entity is reserved by someone/some other state
    public boolean isAlreadyReserved(Entity entityToCheck) {
        ComponentMapper<ReservedComponent> reservedComponents = ComponentMapper.getFor(ReservedComponent.class);
        if (reservedComponents.has(entityToCheck)) {
            if (reservedComponents.get(entityToCheck).getReservedBy() != entity) {
                return true;
            }
        }
        return false;
    }

    public enum StateStatus {
        IDLE,
        RUNNING,
        EXITED
    }
}
