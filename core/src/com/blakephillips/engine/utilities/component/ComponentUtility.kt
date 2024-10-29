package com.blakephillips.engine.utilities.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import kotlin.reflect.KClass

object ComponentUtility {
    private fun <T : Component> getFor(type: Class<T>): ComponentMapper<T> =
        ComponentMapper.getFor(type) ?: throw Error("ComponentMapper.getFor(...) returned null for ${type::class.simpleName}")

    fun <T : Component> KClass<T>.getFor(): ComponentMapper<T> =
        getFor(this.java)
}