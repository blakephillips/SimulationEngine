package com.blakephillips.game;

import com.badlogic.ashley.core.Engine;

//Global static access
public class Orchestrator {

    private static final Engine engine = new Engine();

    public static Engine getEngine() {
        return engine;
    }

    public static boolean gameIgnoreInput = false;

}
