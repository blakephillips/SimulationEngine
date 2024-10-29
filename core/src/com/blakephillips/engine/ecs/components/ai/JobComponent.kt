package com.blakephillips.engine.ecs.components.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.blakephillips.engine.ai.State
import com.blakephillips.game.data.JobStatus

class JobComponent(val name: String, var status: JobStatus, rootState: State) : Component {
    val stateEntity = Entity()
    var currentState = rootState
}