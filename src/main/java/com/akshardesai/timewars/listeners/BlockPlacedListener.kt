package com.akshardesai.timewars.listeners

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.util.Vector

class BlockPlacedListener : Listener {

    @EventHandler
    fun onBlockPlaced(event: BlockPlaceEvent) {
        val block = event.blockAgainst
        if (block.type == Material.END_PORTAL_FRAME && event.player.gameMode != GameMode.CREATIVE) {
            event.isCancelled = true
        }
        if (event.blockPlaced.type == Material.TNT) {
            event.isCancelled = true
            val tntBlock = event.player.location.world.spawnEntity(event.blockPlaced.location.add(0.5,0.5,0.5), EntityType.PRIMED_TNT) as TNTPrimed
            tntBlock.fuseTicks = 60
            tntBlock.velocity = Vector(0.0,tntBlock.velocity.y,0.0)
            val item = event.player.inventory.itemInMainHand.clone()
            item.amount = item.amount-1
            event.player.inventory.setItemInMainHand(item)
        }
    }
}