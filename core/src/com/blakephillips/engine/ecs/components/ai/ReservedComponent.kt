package com.blakephillips.engine.ecs.components.ai

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

//An object has been reserved by another actors state
class ReservedComponent(val reservedBy: Entity) : Component
