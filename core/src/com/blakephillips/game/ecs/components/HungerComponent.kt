package com.blakephillips.game.ecs.components

import com.badlogic.ashley.core.Component

class HungerComponent : Component {
    var hunger = 0
        set(value) {
            if (value > 100) {
                field = 100
                return
            }
            field = value
        }
}
