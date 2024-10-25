package com.blakephillips.engine.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.blakephillips.engine.ecs.components.ai.ReservedComponent;

public abstract class State {
    public Entity entity;
    private StateStatus stateStatus = StateStatus.IDLE;
    private State previousState = null;
    private State nextState = null;

    public State(Entity entity) {
        this.entity = entity;
    }

    public void setAllEntities(Entity entity) {
        State state = this;
        state.entity = entity;
        while (state.getNextState() != null) {
            state = state.getNextState();
            state.entity = entity;
        }
    }

    public abstract void enter();

    public void exit() {
        stateStatus = StateStatus.COMPLETE;
    }

    public abstract void update(float deltaTime);

    public void reserveEntity(Entity entityToReserve) {
        entityToReserve.add(new ReservedComponent(entity));
    }

    //Check if this entity is reserved by someone/some other state
    public boolean isAlreadyReserved(Entity entityToCheck) {
        ComponentMapper<ReservedComponent> reservedComponents = ComponentMapper.getFor(ReservedComponent.class);
        if (reservedComponents.has(entityToCheck)) {
            return !reservedComponents.get(entityToCheck).getReservedBy().equals(entity);
        }
        return false;
    }

    public void exit(boolean failed) {
        exit();
        if (failed) {
            stateStatus = StateStatus.FAILED;
        }
    }

    public void setPreviousState(State state) {
        previousState = state;
        state.nextState = this;
    }

    public State getPreviousState() {
        return previousState;
    }

    public void setNextState(State state) {
        nextState = state;
        state.previousState = this;
    }

    public State getNextState() {
        return nextState;
    }

    public void setStateStatus(StateStatus status) {
        if (this.stateStatus == StateStatus.FAILED || this.stateStatus == StateStatus.COMPLETE) {
            return;
        }
        this.stateStatus = status;
    }

    public StateStatus getStateStatus() {
        return stateStatus;
    }

    public enum StateStatus {
        IDLE,
        PAUSED,
        RUNNING,
        COMPLETE,
        FAILED
    }
}
