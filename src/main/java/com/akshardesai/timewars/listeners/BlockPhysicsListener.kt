package com.akshardesai.timewars.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent

class BlockPhysicsListener : Listener {
    @EventHandler
    fun onGrassConvertDirt(event: BlockPhysicsEvent) {
        if (event.block.type == Material.GRASS_BLOCK || event.block.type == Material.DIRT) {
            event.isCancelled = true
        }
    }
}