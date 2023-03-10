package com.blakephillips.game.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.UIState

class InputSystem: EntitySystem() {
    override fun update(deltaTime: Float) {
        if (Orchestrator.uiState == UIState.SELECTING)
        {

        }
    }
}