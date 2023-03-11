package com.blakephillips.game

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.blakephillips.game.data.UIState

//Global static access
object Orchestrator {
    @JvmField
    val engine = Engine()
    @JvmField
    var gameIgnoreInput = false
    @JvmField
    var uiState = UIState.DEFAULT
    @JvmField
    var camera = OrthographicCamera(680F, 480F).also {
        it.position.set(320f, 240f, 0f)
        it.setToOrtho(false, 680f, 480f)
    }
    @JvmField
    var viewport = FitViewport(640f, 480f, camera)
}