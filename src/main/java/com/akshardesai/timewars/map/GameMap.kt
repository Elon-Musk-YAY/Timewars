package com.akshardesai.timewars.map

import org.bukkit.World

interface GameMap {
    fun load(): Boolean
    fun unload()
    fun restoreFromSource(): Boolean
    val isLoaded: Boolean
    val world: World?
}