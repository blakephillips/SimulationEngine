package com.blakephillips.engine.ecs.systems.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.blakephillips.engine.ai.State;
import com.blakephillips.engine.ecs.components.ai.JobComponent;
import com.blakephillips.engine.ecs.components.ai.StateComponent;

public class JobSystem extends IteratingSystem {

    ComponentMapper<JobComponent> jobComponents = ComponentMapper.getFor(JobComponent.class);
    public JobSystem() {
        super(Family.all(JobComponent.class).get());
    }
    @Override
    protected void processEntity(Entity entity, float v) {
        JobComponent job = jobComponents.get(entity);
        if (job.status == JobComponent.JobStatus.START_PENDING) {
            job.getStateEntity().add(new StateComponent(job.getCurrentState()));
            getEngine().addEntity(job.getStateEntity());
            job.status = JobComponent.JobStatus.RUNNING;
        }

        if (job.status == JobComponent.JobStatus.RUNNING) {
            if (job.getCurrentState().stateStatus == State.StateStatus.COMPLETE) {
                if (job.getCurrentState().getNextState() == null) {
                    job.getStateEntity().remove(StateComponent.class);
                    job.status = JobComponent.JobStatus.FINISHED;
                }

                if (job.getCurrentState().getNextState() != null) {
                    job.getStateEntity().add(new StateComponent(job.getCurrentState().getNextState()));
                    job.setCurrentState(job.getCurrentState().getNextState());
                }
            }
        }

        //just quit cancelled and incomplete jobs for now
        if (job.status == JobComponent.JobStatus.FINISHED
                || job.status == JobComponent.JobStatus.CANCELLED
                || job.status == JobComponent.JobStatus.INCOMPLETE) {
            getEngine().removeEntity(job.getStateEntity());
            getEngine().removeEntity(entity);
            Gdx.app.log("Game", String.format("Finished job %s", job.getName()));
        }
    }
}
