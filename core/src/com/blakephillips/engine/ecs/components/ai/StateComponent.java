package com.blakephillips.engine.ecs.components.ai;

import com.badlogic.ashley.core.Component;
import com.blakephillips.engine.ai.State;

public class StateComponent implements Component {
    public State state;

    public StateComponent(State state) {
        this.state = state;
    }
}
