package com.blakephillips.game.ecs.components

import com.badlogic.ashley.core.Component
import com.blakephillips.engine.ecs.components.position.PositionComponent

class SelectableComponent(var pos: PositionComponent, var selected: Boolean = false): Component