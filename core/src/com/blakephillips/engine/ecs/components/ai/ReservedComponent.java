package com.blakephillips.engine.ecs.components.ai;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

//An object has been reserved by another actors state
public class ReservedComponent implements Component {
    private Entity reservedBy;
    public ReservedComponent(Entity reservedBy) {
        this.reservedBy = reservedBy;
    }

    public void setReservedBy(Entity reservedBy) {
        this.reservedBy = reservedBy;
    }
    public Entity getReservedBy() {
        return reservedBy;
    }
}
