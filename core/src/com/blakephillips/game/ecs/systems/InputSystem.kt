package com.blakephillips.game.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.ecs.systems.mouse.MousePositionSystem import com.blakephillips.game.Orchestrator
import com.blakephillips.game.data.ObjectType
import com.blakephillips.game.data.UIState
import com.blakephillips.game.ui.BoxSelection

class InputSystem: EntitySystem() {
    var boxSelector: BoxSelection = BoxSelection(Vector2(0f,0f), ObjectType.TREE)
    val mouseSystem = Orchestrator.engine.getSystem(MousePositionSystem::class.java)
    override fun update(deltaTime: Float) {

        if (Orchestrator.uiState == UIState.SELECTING)
        {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                val mPos = mouseSystem.unprojectedMousePos()
                boxSelector.setStartPosition(mPos.x, mPos.y)
                boxSelector.visible(mPos.x, mPos.y)
            }
            else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                val mPos = mouseSystem.unprojectedMousePos()
                boxSelector.setEndPosition(mPos.x, mPos.y)
            }
            else {
                boxSelector.remove()
            }

        }

    }

    companion object {
        private lateinit var boxSelector: BoxSelection
    }
}