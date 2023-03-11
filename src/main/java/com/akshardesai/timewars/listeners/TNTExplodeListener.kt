package com.akshardesai.timewars.listeners

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class TNTExplodeListener: Listener {
    @EventHandler
    fun onTntExplode(event: EntityExplodeEvent) {
        val exceptions = listOf(Material.OAK_PLANKS,Material.END_STONE, Material.WHITE_WOOL, Material.GRAY_WOOL, Material.RED_WOOL,Material.BLUE_WOOL,Material.LIME_WOOL,Material.CYAN_WOOL,Material.YELLOW_WOOL,Material.PINK_WOOL)
        val destroyed: MutableList<*> = event.blockList()
        val it = destroyed.iterator()
        while (it.hasNext()) { // Loop trough blocks
            val block = it.next() as Block // Get block
            if (!exceptions.contains(block.type)) { // If the block is in the Array
                it.remove() // Remove the block from the blockList
            }
        }
    }
}