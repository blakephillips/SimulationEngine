package com.blakephillips.game;

import com.badlogic.ashley.core.Engine;
import com.blakephillips.game.data.UIState;

//Global static access
public class Orchestrator {

    private static final Engine engine = new Engine();

    public static Engine getEngine() {
        return engine;
    }

    public static boolean gameIgnoreInput = false;

    public static UIState uiState = UIState.DEFAULT;

}
