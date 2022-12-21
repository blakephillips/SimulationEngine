package com.blakephillips.game.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.blakephillips.engine.ecs.components.ai.CurrentJobComponent;
import com.blakephillips.engine.ecs.components.ai.JobComponent;
import com.blakephillips.engine.utilities.datatype.Pair;
import com.blakephillips.game.Orchestrator;
import com.blakephillips.game.data.JobType;
import com.blakephillips.game.ecs.components.JobTypeComponent;


import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;


public class QueueSystem extends EntitySystem {
    private final HashMap<JobType, PriorityQueue<Pair<Integer, Entity>>> queue = new HashMap<>();
    private final ComponentMapper<CurrentJobComponent> currentJobComponents = ComponentMapper.getFor(CurrentJobComponent.class);
    private final ComponentMapper<JobComponent> jobComponents = ComponentMapper.getFor(JobComponent.class);
    private final ComponentMapper<JobTypeComponent> jobTypeComponents = ComponentMapper.getFor(JobTypeComponent.class);
    private ImmutableArray<Entity> entities;

    public QueueSystem() {
        Orchestrator.getEngine().addEntityListener(Family.all(JobComponent.class).get(), new QueueListener());
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(CurrentJobComponent.class).get());
    }

    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity assignableEntity = entities.get(i);
            CurrentJobComponent currentJobComponent = currentJobComponents.get(assignableEntity);
            // if the current job is running / in any non-finished state, skip
            if (currentJobComponent.currentJob != null &&
                    currentJobComponent.currentJob.status != JobComponent.JobStatus.CANCELLED &&
                    currentJobComponent.currentJob.status != JobComponent.JobStatus.FINISHED) {
                continue;
            }

            currentJobComponent.currentJob = null;

            for (JobType jobType: queue.keySet()) {
                PriorityQueue<Pair<Integer, Entity>> jobQueue = queue.get(jobType);

                if (jobQueue.isEmpty()) { continue; }

                // TODO: job preferences
                Entity queuedJobEntity = jobQueue.poll().getValue();
                JobComponent queuedJobComponent = jobComponents.get(queuedJobEntity);

                // take it from idle state to awaiting start
                queuedJobComponent.status = JobComponent.JobStatus.START_PENDING;

                // set the current job to the new one
                currentJobComponent.currentJob = queuedJobComponent;
                queuedJobComponent.getCurrentState().setAllEntities(assignableEntity);
            }
        }
    }

    public void addJob(Entity entity) {
        if (!hasJobComponent(entity)) { return; }
        if (!hasJobTypeComponent(entity)) { return; }
        JobComponent jobComponent = jobComponents.get(entity);

        if (jobComponent.status != JobComponent.JobStatus.IDLE) {
            Gdx.app.debug("QueueSystem", String.format("Job %s was added to engine not in idle state, " +
                    "skipping adding to queue.", jobComponent.getName()));
            return;
        }

        JobTypeComponent jobTypeComponent = jobTypeComponents.get(entity);

        if (!queue.containsKey(jobTypeComponent.type)) {
            queue.put(jobTypeComponent.type, new PriorityQueue<>(Comparator.comparing(Pair::getKey)));
        }

        //TODO: Priority
        jobComponent.status = JobComponent.JobStatus.IDLE;
        queue.get(jobTypeComponent.type).add(new Pair<>(5, entity));
        Gdx.app.debug("QueueSystem", String.format("Added job: %s to queue", jobComponent.getName()));
    }

    private boolean hasJobComponent(Entity entity) {
        if (!jobComponents.has(entity)) {
            Gdx.app.error("QueueSystem", "Job doesn't have JobComponent");
            return false;
        }
        return true;
    }

    private boolean hasJobTypeComponent(Entity entity) {
        if (!jobTypeComponents.has(entity)) {
            Gdx.app.error("QueueSystem", "Job doesn't have JobTypeComponent");
            return false;
        }
        return true;
    }
}

class QueueListener implements EntityListener {

    @Override
    public void entityAdded(Entity entity) {
        Orchestrator.getEngine().getSystem(QueueSystem.class).addJob(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {}
}
