package com.akshardesai.timewars.listeners


import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockDestroyedListener : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (event.player.gameMode != GameMode.CREATIVE && block.type != Material.WHITE_WOOL) {
            when (block.type) {
                Material.OAK_PLANKS,Material.END_STONE, Material.WHITE_WOOL, Material.GRAY_WOOL, Material.RED_WOOL,Material.BLUE_WOOL,Material.LIME_WOOL,Material.CYAN_WOOL,Material.YELLOW_WOOL,Material.PINK_WOOL -> {
                } else -> {
                event.isCancelled = true
            }
            }
        }
        if (block.type == Material.END_PORTAL_FRAME && event.player.gameMode != GameMode.CREATIVE) {
            event.isCancelled = true
        }
    }
}


