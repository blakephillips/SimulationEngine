package com.blakephillips.engine.ecs.systems.ai

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.blakephillips.engine.ai.State
import com.blakephillips.engine.ai.State.StateStatus
import com.blakephillips.engine.ecs.components.ai.JobComponent
import com.blakephillips.engine.ecs.components.ai.StateComponent

class JobSystem : IteratingSystem(Family.all(JobComponent::class.java).get()) {
    private var jobComponents = ComponentMapper.getFor(JobComponent::class.java)
    override fun processEntity(entity: Entity, v: Float) {
        val job = jobComponents[entity]

        when (job.status) {
            JobComponent.JobStatus.START_PENDING -> {
                job.stateEntity.add(StateComponent(job.currentState))
                engine.addEntity(job.stateEntity)
                job.status = JobComponent.JobStatus.RUNNING
            }
            JobComponent.JobStatus.RUNNING -> {
                when (job.currentState.stateStatus) {
                    StateStatus.COMPLETE -> {
                        if (job.currentState.nextState == null) {
                            job.stateEntity.remove(StateComponent::class.java)
                            job.status = JobComponent.JobStatus.FINISHED
                        }
                        if (job.currentState.nextState != null) {
                            job.stateEntity.add(StateComponent(job.currentState.nextState))
                            job.currentState = job.currentState.nextState
                        }
                    }
                    StateStatus.FAILED -> {
                        Gdx.app.debug("JobSystem", "Job '${job.name}' failed")
                        job.stateEntity.remove(StateComponent::class.java)
                        job.status = JobComponent.JobStatus.INCOMPLETE
                    }
                    else -> {}
                }
            }
            JobComponent.JobStatus.FINISHED, JobComponent.JobStatus.CANCELLED, JobComponent.JobStatus.INCOMPLETE -> {
                engine.removeEntity(job.stateEntity)
                engine.removeEntity(entity)
                Gdx.app.log("Game", "Finished job ${job.name}")
            }
            JobComponent.JobStatus.IDLE, null -> {}
        }
    }
}