package com.blakephillips.engine.ai;

import com.badlogic.ashley.core.Entity;

/**
 * A state that gathers its requirements sometime in the future.
 * Example: A haul state that doesn't care what exact resource it uses until
 * the haul state is running on an entity
 */
public abstract class DeferrableState extends State {
    boolean gatheredRequirements = false;
    public DeferrableState(Entity entity) {
        super(entity);
    }

    @Override
    public void enter() {
        setStateStatus(StateStatus.RUNNING);
    }

    /**
     * Gather the deferred requirements that are needed by the state.
     * This method is invoked within the abstract class when the StateStatus is set to RUNNING
     */
    protected abstract void gatherRequirements();

    /**
     * Setter that enacts gatherRequirements the first time a state is set to running
     * @param status - The state to assign to the status
     */
    @Override
    public void setStateStatus(StateStatus status) {

        if (status == StateStatus.RUNNING && !gatheredRequirements) {
            gatherRequirements();
            gatheredRequirements = true;
        }
        super.setStateStatus(status);
    }
}
