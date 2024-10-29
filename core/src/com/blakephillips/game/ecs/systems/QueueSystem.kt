package com.blakephillips.game.ecs.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.blakephillips.engine.ecs.components.ai.CurrentJobComponent
import com.blakephillips.engine.ecs.components.ai.JobComponent
import com.blakephillips.engine.utilities.datatype.Pair
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.JobStatus
import com.blakephillips.game.data.JobType
import com.blakephillips.game.data.isInProgress
import com.blakephillips.game.ecs.components.JobTypeComponent
import java.util.PriorityQueue

class QueueSystem : EntitySystem() {
    private val queueMap = HashMap<JobType, PriorityQueue<Pair<Int, Entity>>>()
    private val currentJobComponents = ComponentMapper.getFor(
        CurrentJobComponent::class.java
    )
    private val jobComponents = ComponentMapper.getFor(JobComponent::class.java)
    private val jobTypeComponents = ComponentMapper.getFor(
        JobTypeComponent::class.java
    )
    private lateinit var entities: ImmutableArray<Entity>

    init {
        Orchestrator.engine.addEntityListener(Family.all(JobComponent::class.java).get(), QueueListener())
    }

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(Family.all(CurrentJobComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (assignableEntity in entities) {
            val currentJobComponent = currentJobComponents[assignableEntity]

            // if the current job is running / in any non-finished state, skip
            val status = currentJobComponent.currentJob?.status
            if (status !== null && status.isInProgress()) continue

            currentJobComponent.currentJob = null
            for ((_, jobQueue) in queueMap) {
                if (jobQueue.isEmpty()) {
                    continue
                }

                // TODO: job preferences
                val queuedJobEntity = jobQueue.poll().value
                val queuedJobComponent = jobComponents[queuedJobEntity]

                // take it from idle state to awaiting start
                queuedJobComponent.status = JobStatus.START_PENDING

                // set the current job to the new one
                currentJobComponent.currentJob = queuedJobComponent
                queuedJobComponent.currentState.setAllEntities(assignableEntity)
            }
        }
    }

    fun addJob(entity: Entity) {
        if (!hasJobTypeComponent(entity)) {
            Gdx.app.error("QueueSystem", "Job was added without JobType, removing from Engine")
            engine.removeEntity(entity)
            return
        }

        val jobComponent = jobComponents[entity]
        if (jobComponent.status != JobStatus.IDLE) {
            Gdx.app.debug(
                "QueueSystem",
                    "Job ${jobComponent.name} was added to engine not in idle state, " +
                            "skipping adding to queue and deleting"
            )
            engine.removeEntity(entity)
            return
        }
        val jobTypeComponent = jobTypeComponents[entity]
        val jobTypeQueue = getJobTypeQueue(queueMap, jobTypeComponent.type)

        //TODO: Priority
        jobComponent.status = JobStatus.IDLE
        jobTypeQueue.add(Pair(5, entity))
        Gdx.app.debug("QueueSystem", String.format("Added job: '%s' to queue", jobComponent.name))
    }

    private fun getJobTypeQueue(
        queueMap: HashMap<JobType, PriorityQueue<Pair<Int, Entity>>>,
        jobType: JobType
    ): PriorityQueue<Pair<Int, Entity>> =
        queueMap[jobType] ?: PriorityQueue(Comparator.comparing { obj: Pair<Int, Entity> -> obj.key }).also {
            queueMap[jobType] = it
        }

    fun clearQueue() {
        for (jobQueuePair in queueMap) {
            jobQueuePair.value.clear()
        }
        Gdx.app.debug("QueueSystem", "Cleared Queue")
    }

    private fun hasJobTypeComponent(entity: Entity): Boolean {
        if (jobComponents.has(entity)) {
            return true
        }

        Gdx.app.error("QueueSystem", "Entity $entity have JobTypeComponent")
        return false
    }
}

internal class QueueListener : EntityListener {
    override fun entityAdded(entity: Entity) {
        Orchestrator.engine.getSystem(QueueSystem::class.java).addJob(entity)
    }

    override fun entityRemoved(entity: Entity) {}
}