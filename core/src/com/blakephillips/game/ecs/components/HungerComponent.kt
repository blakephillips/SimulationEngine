package com.blakephillips.game.ecs.components;

import com.badlogic.ashley.core.Component;

public class HungerComponent implements Component {
    private int hungerValue;

    public int getHunger() {
        return hungerValue;
    }

    public void setHungerValue(int hungerValue) {
        if (hungerValue > 100) {
            this.hungerValue = 100;
            return;
        }

        this.hungerValue = hungerValue;
    }

}
