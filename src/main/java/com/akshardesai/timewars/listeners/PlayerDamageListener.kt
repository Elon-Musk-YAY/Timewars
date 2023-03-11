package com.akshardesai.timewars.listeners

import com.akshardesai.timewars.TimeWars
import com.akshardesai.timewars.team.TeamManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration

class PlayerDamageListener : Listener {
    @EventHandler
    fun onPlayerDamaged(event: EntityDamageByEntityEvent) {
        if (event.damager is Player && event.entity is Player) {
            TimeWars.combatTags[event.entity as Player] = event.damager as Player
            if (TimeWars.combatTagEvents[event.entity as Player] != null) {
                TimeWars.combatTagEvents[event.entity as Player]?.cancel()
            }
            TimeWars.combatTagEvents[event.entity as Player] = object: BukkitRunnable() {
                override fun run() {
                    TimeWars.combatTags[event.entity as Player] = null
                }
            }.runTaskLater(TimeWars.instance as JavaPlugin,60L)
            if ((event.entity as Player).hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                TimeWars.invisPlayers[event.damager as Player]?.cancel()
                for (online in Bukkit.getServer().onlinePlayers) {
                    if (online == event.entity as Player) continue
                    online.sendEquipmentChange(event.entity as Player, EquipmentSlot.HEAD, (event.entity as Player).inventory.helmet as ItemStack)
                    online.sendEquipmentChange(event.entity as Player, EquipmentSlot.CHEST, (event.entity as Player).inventory.chestplate as ItemStack)
                    online.sendEquipmentChange(event.entity as Player, EquipmentSlot.LEGS, (event.entity as Player).inventory.leggings as ItemStack)
                    online.sendEquipmentChange(event.entity as Player, EquipmentSlot.FEET, (event.entity as Player).inventory.boots as ItemStack)
                    online.sendEquipmentChange(event.entity as Player, EquipmentSlot.HAND, (event.entity as Player).inventory.itemInMainHand)
                    online.sendEquipmentChange(event.entity as Player, EquipmentSlot.OFF_HAND, (event.entity as Player).inventory.itemInOffHand)
                }
                (event.entity as Player).removePotionEffect(PotionEffectType.INVISIBILITY)
                event.entity.sendMessage(Component.text("${ChatColor.RED}You took damage and lost your invisibility!"))
            }
        }
    }



    @EventHandler
    fun onPlayerKilled(event: PlayerDeathEvent) {
        if (event.player.killer == null && event.entity.lastDamageCause?.cause == EntityDamageEvent.DamageCause.FALL) {
            Bukkit.getServer().sendMessage(
                Component.text(
                    "${TeamManager.getTeamColor(event.player)}${event.player.name}${ChatColor.GRAY} slipped off a cliff."
                )
            )
        } else if (event.entity.lastDamageCause?.cause == EntityDamageEvent.DamageCause.FALL && event.player.killer != null) {
            TimeWars.addKill(event.player,Component.text(
                "${TeamManager.getTeamColor(event.player)}${event.player.name}${ChatColor.GRAY} was knocked off a cliff by ${
                    TeamManager.getTeamColor(event.player.killer as Player)
                }${event.player.killer?.name}${ChatColor.GRAY}."
            ))

        }
        else {
            TimeWars.addKill(event.player,Component.text(
                "${TeamManager.getTeamColor(event.player)}${event.player.name}${ChatColor.GRAY} was killed by ${
                    TeamManager.getTeamColor(event.player.killer ?: return)
                }${event.player.killer?.name}${ChatColor.GRAY}."
            ))
        }
        event.isCancelled = true
        event.player.gameMode = GameMode.SPECTATOR
        if (event.entity.lastDamageCause?.cause != EntityDamageEvent.DamageCause.FALL) {
            event.player.health = 20.0
            event.player.teleport(Location(Bukkit.getServer().worlds[0],0.5,172.0,0.5))
        } else if (event.entity.lastDamageCause?.cause == EntityDamageEvent.DamageCause.FALL) {
            event.player.velocity.y = -1.5
        }
        for (effect in event.player.activePotionEffects) {
            event.player.removePotionEffect(effect.type)
        }
        event.player.sendTitlePart(
            TitlePart.TIMES, Title.Times.times(
                Duration.ofSeconds(1L),
                Duration.ofSeconds(4L),
                Duration.ofSeconds(1L)))
        event.player.sendTitlePart(TitlePart.TITLE,Component.text(ChatColor.RED.toString()+"YOU DIED!"))
        var respawnTime = 6
        object: BukkitRunnable() {
            override fun run() {
                respawnTime--
                if (respawnTime == 0) {
                    event.player.gameMode = GameMode.SURVIVAL
                    this.cancel()
                    event.player.teleport(TeamManager.getTeam(event.player)?.spawnLocation as Location)
                    event.player.sendTitlePart(
                        TitlePart.TIMES, Title.Times.times(
                            Duration.ofSeconds(1L),
                            Duration.ofSeconds(1L),
                            Duration.ofSeconds(1L)))
                    event.player.sendTitlePart(TitlePart.TITLE,Component.text(ChatColor.GREEN.toString()+"RESPAWNED!"))
                    event.player.sendTitlePart(TitlePart.SUBTITLE,Component.text(""))
                    event.player.sendTitlePart(
                        TitlePart.TIMES, Title.Times.times(
                            Duration.ofSeconds(1L),
                            Duration.ofSeconds(4L),
                            Duration.ofSeconds(1L)))



                } else {
                    event.player.sendMessage(Component.text(ChatColor.YELLOW.toString() + "You will respawn in ${ChatColor.RED}$respawnTime${ChatColor.YELLOW} second${if (respawnTime == 1) "" else "s"}!"))
                    event.player.sendTitlePart(
                        TitlePart.SUBTITLE,
                        Component.text(ChatColor.YELLOW.toString() + "You will respawn in ${ChatColor.RED}$respawnTime${ChatColor.YELLOW} second${if (respawnTime == 1) "" else "s"}!")
                    )
                }
            }
        }.runTaskTimer(TimeWars.instance as JavaPlugin,0L,20L)
    }
}