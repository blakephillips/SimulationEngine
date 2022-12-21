package com.blakephillips.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class HoldingItemComponent implements Component {
    public Entity heldItem;

    public HoldingItemComponent(Entity toHold) {
        heldItem = toHold;
    }

}
