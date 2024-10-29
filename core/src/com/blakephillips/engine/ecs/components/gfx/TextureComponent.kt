package com.blakephillips.engine.ecs.components.gfx

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TextureComponent(var region: TextureRegion, var z: Int) : Component
