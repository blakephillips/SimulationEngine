package com.blakephillips.engine.ecs.components.ai;

import com.badlogic.ashley.core.Component;

public class CurrentJobComponent implements Component {
    public JobComponent currentJob;

    public CurrentJobComponent(JobComponent currentJob) {
        this.currentJob = currentJob;
    }

}
