package com.blakephillips.engine.ecs.components.ai;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.blakephillips.engine.ai.State;

public class JobComponent implements Component {
    private String name;
    private final State rootState;
    private State currentState;
    private final Entity stateEntity;
    public JobStatus status;

    public JobComponent(String jobName, JobStatus jobStatus, State rootState) {
        setName(jobName);
        status = jobStatus;
        this.rootState = rootState;
        currentState = rootState;
        stateEntity = new Entity();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        currentState = state;
    }

    public Entity getStateEntity() {
        return stateEntity;
    }

    public enum JobStatus {
        IDLE,
        START_PENDING,
        RUNNING,
        FINISHED,
        INCOMPLETE,
        CANCELLED
    }

}
