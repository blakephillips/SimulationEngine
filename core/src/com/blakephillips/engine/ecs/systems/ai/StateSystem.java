package com.blakephillips.engine.ecs.systems.ai;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.blakephillips.engine.ai.State;
import com.blakephillips.engine.ecs.components.ai.StateComponent;

public class StateSystem extends IteratingSystem {

    ComponentMapper<StateComponent> stateComponents = ComponentMapper.getFor(StateComponent.class);

    public StateSystem() {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateComponent stateComponent = stateComponents.get(entity);

        if (stateComponent.state.stateStatus == State.StateStatus.IDLE) {
            stateComponent.state.enter();
        }
        if (stateComponent.state.stateStatus == State.StateStatus.RUNNING) {
            stateComponent.state.update(deltaTime);
        }
        // for now, just remove the state completely when exited.
        else if (stateComponent.state.stateStatus == State.StateStatus.EXITED) {
            entity.remove(StateComponent.class);
        }
    }
}
