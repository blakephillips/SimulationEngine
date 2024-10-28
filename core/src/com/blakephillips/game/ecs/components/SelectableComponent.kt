package com.blakephillips.game.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

class SelectableComponent(
    var selected: Boolean = false,
    var selectionEntity: Entity? = null
) : Component