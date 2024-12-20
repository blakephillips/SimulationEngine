package com.blakephillips.engine.ecs.components

import com.badlogic.ashley.core.Component
import com.blakephillips.engine.utilities.grid.Vertex
import space.earlygrey.simplegraphs.Path

class PathComponent(@JvmField val path: Path<Vertex>) : Component
