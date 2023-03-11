package com.akshardesai.timewars.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import com.akshardesai.timewars.TimeWars
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector

class StartGameCommand : CommandExecutor {
    private val dropSpeed = Vector(0f,-0.1f,0f)
    private var ironSpawner: Int = 0
    private var diamondSpawner: Int = 0
    private var emeraldSpawner: Int = 0
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.hasPermission("timewars.start")) {
                if (!TimeWars.gameStarted) {
                    TimeWars.diamondCD = 10
                    TimeWars.emeraldCD = 20
                    TimeWars.gameStarted = true
                    val worlds: List<World> = player.server.worlds
                    for (w in worlds) {
                        TimeWars.map_time[w.name] = w.time
                    }
                    // Iron Spawning
                    ironSpawner = player.server.scheduler.scheduleSyncRepeatingTask(TimeWars.instance as JavaPlugin, {
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, -80.0, 89.0, 51.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, 80.0, 89.0, 51.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, 80.0, 89.0, -51.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, -80.0, 89.0, -51.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, 51.0, 89.0, -80.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, 51.0, 89.0, 80.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, -51.0, 89.0, 80.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed
                        TimeWars.map.world!!.dropItem(
                            Location(TimeWars.map.world, -51.0, 89.0, -80.0).add(0.5, 2.2, 0.5), ItemStack(
                                Material.IRON_INGOT)
                        ).velocity = dropSpeed

                    }, 0L, 40L)
                    // Diamond Spawning
                    diamondSpawner = player.server.scheduler
                        .scheduleSyncRepeatingTask(TimeWars.instance as JavaPlugin, {
                            if (TimeWars.diamondCD == 0) {
                                TimeWars.diamondCD = 10
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, -100.0, 88.0, 0.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.DIAMOND
                                    )
                                ).velocity = dropSpeed
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, 0.0, 88.0, 100.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.DIAMOND
                                    )
                                ).velocity = dropSpeed
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, 100.0, 88.0, 0.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.DIAMOND
                                    )
                                ).velocity = dropSpeed
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, 0.0, 88.0, -100.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.DIAMOND
                                    )
                                ).velocity = dropSpeed
                            }
                            else {TimeWars.diamondCD--}

                            for (hologram: ArmorStand in TimeWars.diamondHolograms) {
                                hologram.customName(Component.text(ChatColor.GOLD.toString()+"Spawning in: "+ChatColor.AQUA.toString()+"${TimeWars.diamondCD}"+ChatColor.GOLD.toString()+" seconds"))
                            }
                        }, 0L, 20L)
                    // Emerald Spawning
                    emeraldSpawner = player.server.scheduler
                        .scheduleSyncRepeatingTask(TimeWars.instance as JavaPlugin, {
                            if (TimeWars.emeraldCD == 0) {
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, 17.0, 93.0, 8.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.EMERALD
                                    )
                                ).velocity = dropSpeed
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, -8.0, 93.0, 17.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.EMERALD
                                    )
                                ).velocity = dropSpeed
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, -17.0, 93.0, -8.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.EMERALD
                                    )
                                ).velocity = dropSpeed
                                TimeWars.map.world!!.dropItem(
                                    Location(TimeWars.map.world, 8.0, 93.0, -17.0).add(0.5, 2.2, 0.5), ItemStack(
                                        Material.EMERALD
                                    )
                                ).velocity = dropSpeed
                                TimeWars.emeraldCD = 20
                            }
                            else {TimeWars.emeraldCD--}
                            for (hologram: ArmorStand in TimeWars.emeraldHolograms) {
                                hologram.customName(Component.text(ChatColor.GOLD.toString()+"Spawning in: "+ChatColor.GREEN.toString()+"${TimeWars.emeraldCD}"+ChatColor.GOLD.toString()+" seconds"))
                            }

                        }, 0L, 20L)

                    player.sendMessage(ChatColor.GREEN.toString() + "Game has started!")
                } else {
                    TimeWars.gameStarted = false
                    Bukkit.getServer().scheduler.cancelTask(ironSpawner)
                    Bukkit.getServer().scheduler.cancelTask(diamondSpawner)
                    Bukkit.getServer().scheduler.cancelTask(emeraldSpawner)
                    TimeWars.diamondCD = 10
                    TimeWars.emeraldCD = 20
                    player.sendMessage(ChatColor.RED.toString() + "Game stopped")
                }
            } else {
                player.sendMessage(ChatColor.RED.toString()+"You must have the timewars.start permission to start the game!")
            }
        }
        return true
    }
}