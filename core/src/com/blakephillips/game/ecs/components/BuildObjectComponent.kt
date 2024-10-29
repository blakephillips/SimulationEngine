package com.blakephillips.game.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.blakephillips.game.data.ResourceType

class BuildObjectComponent(val typeMap: Map<ResourceType, Int>, val objectEntity: Entity) : Component
