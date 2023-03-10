package com.blakephillips.game.ecs.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.blakephillips.engine.ecs.components.ai.CurrentJobComponent
import com.blakephillips.engine.ecs.components.ai.JobComponent
import com.blakephillips.engine.utilities.datatype.Pair
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.JobType
import com.blakephillips.game.ecs.components.JobTypeComponent
import java.util.*

class QueueSystem : EntitySystem() {
    private val queue = HashMap<JobType, PriorityQueue<Pair<Int, Entity>>>()
    private val currentJobComponents = ComponentMapper.getFor(
        CurrentJobComponent::class.java
    )
    private val jobComponents = ComponentMapper.getFor(JobComponent::class.java)
    private val jobTypeComponents = ComponentMapper.getFor(
        JobTypeComponent::class.java
    )
    private var entities: ImmutableArray<Entity>? = null

    init {
        Orchestrator.getEngine().addEntityListener(Family.all(JobComponent::class.java).get(), QueueListener())
    }

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(Family.all(CurrentJobComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (i in 0 until entities!!.size()) {
            val assignableEntity = entities!![i]
            val currentJobComponent = currentJobComponents[assignableEntity]
            // if the current job is running / in any non-finished state, skip
            when (currentJobComponent.currentJob?.status) {
                JobComponent.JobStatus.RUNNING, JobComponent.JobStatus.START_PENDING -> continue
                else -> {}
            }
            currentJobComponent.currentJob = null
            for ((jobType, jobQueue) in queue) {
                if (jobQueue.isEmpty()) {
                    continue
                }

                // TODO: job preferences
                val queuedJobEntity = jobQueue.poll().value
                val queuedJobComponent = jobComponents[queuedJobEntity]

                // take it from idle state to awaiting start
                queuedJobComponent.status = JobComponent.JobStatus.START_PENDING

                // set the current job to the new one
                currentJobComponent.currentJob = queuedJobComponent
                queuedJobComponent.currentState.setAllEntities(assignableEntity)
            }
        }
    }

    fun addJob(entity: Entity) {
        when {
            !hasJobTypeComponent(entity) -> {
                Gdx.app.error("QueueSystem", "Job was added without JobType, removing from Engine")
                engine.removeEntity(entity)
                return
            }

            else -> {}
        }

        val jobComponent = jobComponents[entity]
        if (jobComponent.status != JobComponent.JobStatus.IDLE) {
            Gdx.app.debug(
                "QueueSystem", String.format(
                    "Job %s was added to engine not in idle state, " +
                            "skipping adding to queue.", jobComponent.name
                )
            )
            return
        }
        val jobTypeComponent = jobTypeComponents[entity]
        if (!queue.containsKey(jobTypeComponent.type)) {
            queue[jobTypeComponent.type] = PriorityQueue(Comparator.comparing { obj: Pair<Int, Entity> -> obj.key })
        }

        //TODO: Priority
        jobComponent.status = JobComponent.JobStatus.IDLE
        queue[jobTypeComponent.type]?.add(Pair(5, entity))
        Gdx.app.debug("QueueSystem", String.format("Added job: '%s' to queue", jobComponent.name))
    }

    fun clearQueue() {
        for (jobQueuePair in queue) {
            jobQueuePair.value.clear()
        }
        Gdx.app.debug("QueueSystem", "Cleared Queue")
    }

    private fun hasJobTypeComponent(entity: Entity): Boolean {
        return when {
            !jobTypeComponents.has(entity) -> {
                Gdx.app.error("QueueSystem", "Job doesn't have JobTypeComponent")
                false
            }

            else -> true
        }
    }
}

internal class QueueListener : EntityListener {
    override fun entityAdded(entity: Entity) {
        Orchestrator.getEngine().getSystem(QueueSystem::class.java).addJob(entity)
    }

    override fun entityRemoved(entity: Entity) {}
}