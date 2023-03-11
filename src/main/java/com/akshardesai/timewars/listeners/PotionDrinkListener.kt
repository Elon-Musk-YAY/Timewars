package com.akshardesai.timewars.listeners

import com.akshardesai.timewars.TimeWars
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable


class PotionDrinkListener : Listener {


    @EventHandler
    fun onChangeItem(event: PlayerItemHeldEvent) {
        if (event.player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            for (online in Bukkit.getServer().onlinePlayers) {
                if (online == event.player) continue
                    online.sendEquipmentChange(event.player, EquipmentSlot.HAND, ItemStack(Material.AIR))
                online.sendEquipmentChange(event.player, EquipmentSlot.OFF_HAND, ItemStack(Material.AIR))
            }
        }
    }
    @EventHandler
    fun onPotionDrink(event: PlayerItemConsumeEvent) {
        if (event.item.type == Material.POTION) {
            when ((event.item.itemMeta as PotionMeta).basePotionData.type) {
                PotionType.INVISIBILITY -> {
                    Bukkit.getServer().scheduler.runTaskLaterAsynchronously(
                        TimeWars.instance as JavaPlugin,
                        Runnable
                        {
                            event.player.inventory.setItemInMainHand(ItemStack(Material.AIR))
                        }, 1L
                    )
                    val effect = PotionEffect(PotionEffectType.INVISIBILITY, 20 * 45, 1,true,false)
                    if (TimeWars.invisPlayers[event.player] != null) {
                        TimeWars.invisPlayers[event.player]?.cancel()

                    }
                    event.player.addPotionEffect(effect)
                    for (online in Bukkit.getOnlinePlayers()) {
                        if (online == event.player) continue
                            online.sendEquipmentChange(event.player, EquipmentSlot.HEAD, ItemStack(Material.AIR))
                            online.sendEquipmentChange(event.player, EquipmentSlot.CHEST, ItemStack(Material.AIR))
                            online.sendEquipmentChange(event.player, EquipmentSlot.LEGS, ItemStack(Material.AIR))
                            online.sendEquipmentChange(event.player, EquipmentSlot.FEET, ItemStack(Material.AIR))
                            online.sendEquipmentChange(event.player, EquipmentSlot.HAND, ItemStack(Material.AIR))
                            online.sendEquipmentChange(event.player, EquipmentSlot.OFF_HAND, ItemStack(Material.AIR))
                        }


                    TimeWars.invisPlayers[event.player] = object : BukkitRunnable() {
                        override fun run() {
                            for (online in Bukkit.getOnlinePlayers()) {
                                if (online == event.player) continue
                                online.sendEquipmentChange(event.player, EquipmentSlot.HEAD, event.player.inventory.helmet as ItemStack)
                                online.sendEquipmentChange(event.player, EquipmentSlot.CHEST, event.player.inventory.chestplate as ItemStack)
                                online.sendEquipmentChange(event.player, EquipmentSlot.LEGS, event.player.inventory.leggings as ItemStack)
                                online.sendEquipmentChange(event.player, EquipmentSlot.FEET, event.player.inventory.boots as ItemStack)
                                online.sendEquipmentChange(event.player, EquipmentSlot.HAND, event.player.inventory.itemInMainHand)
                                online.sendEquipmentChange(event.player, EquipmentSlot.OFF_HAND, event.player.inventory.itemInOffHand)

                            }
                            event.player.removePotionEffect(PotionEffectType.INVISIBILITY)
                        }
                    }.runTaskLater(TimeWars.instance as JavaPlugin, 45 * 20L)

                }

            PotionType.JUMP -> {
                Bukkit.getServer().scheduler.runTaskLaterAsynchronously(
                    TimeWars.instance as JavaPlugin,
                    Runnable
                    {
                        event.player.inventory.setItemInMainHand(ItemStack(Material.AIR))
                    }, 1L
                )
                val effect = PotionEffect(PotionEffectType.JUMP, 20 * 45, 4,true,false)

                event.player.addPotionEffect(effect)
            }

                PotionType.SPEED -> {
                    Bukkit.getServer().scheduler.runTaskLaterAsynchronously(
                        TimeWars.instance as JavaPlugin,
                        Runnable
                        {
                            event.player.inventory.setItemInMainHand(ItemStack(Material.AIR))
                        }, 1L
                    )
                    val effect = PotionEffect(PotionEffectType.SPEED, 20 * 45, 1,true,false)

                    event.player.addPotionEffect(effect)
                }
                else -> {

                }
            }
        }
        event.isCancelled = true
    }
}