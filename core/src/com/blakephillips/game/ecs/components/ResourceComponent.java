package com.blakephillips.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.blakephillips.game.data.ResourceType;

public class ResourceComponent implements Component {
    ResourceType type;

    public ResourceComponent(ResourceType type) {
        this.type = type;
    }

    public ResourceType getType() {
        return type;
    }

}
