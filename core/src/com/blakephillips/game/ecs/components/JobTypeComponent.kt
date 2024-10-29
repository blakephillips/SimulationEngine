package com.blakephillips.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.blakephillips.game.data.JobType;

public class JobTypeComponent implements Component {
    public JobType type;

    public JobTypeComponent(JobType type) {
        this.type = type;
    }

}
