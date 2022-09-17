package com.blakephillips.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.blakephillips.game.data.ResourceType;

import java.util.HashMap;
import java.util.Map;

public class BuildObjectComponent implements Component {
    //key: type of resource needed, value: how many needed
    private Map<ResourceType, Integer> typeMap = new HashMap<>();
    //object to instantiate when the building is complete
    private Entity objectEntity;
    public BuildObjectComponent(Map<ResourceType, Integer> typeMap, Entity objectEntity) {
        this.typeMap = typeMap;
        this.objectEntity = objectEntity;
    }

}
