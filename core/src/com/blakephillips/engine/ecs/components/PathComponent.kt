package com.blakephillips.engine.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.blakephillips.engine.utilities.grid.Vertex
import space.earlygrey.simplegraphs.Path

class PathComponent(@JvmField var path: Path<Vertex>?, @JvmField var destination: Vector2) : Component 
