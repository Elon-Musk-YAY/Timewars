package com.akshardesai.timewars.listeners


import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

class PlayerJoinLeaveListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(Component.text(ChatColor.YELLOW.toString()+"${event.player.name} has joined! Welcome!"))
    }

    @EventHandler
    fun onPlayerSpawn(event: PlayerSpawnLocationEvent) {
            event.spawnLocation = Location(Bukkit.getServer().worlds[0],0.5,81.0,0.5)

    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
//        val board = event.player.scoreboard
//        val team = board.getTeam("health")
//        for (gameTeam in Bukkit.getServer().scoreboardManager.mainScoreboard.teams) {
//            if (gameTeam.hasPlayer(event.player)) {
//                gameTeam.unregister()
//                break
//            }
//        }
//        team?.removePlayer(event.player)
//        if (TeamManager.getTeam(event.player) != null) {
//            TeamManager.removeTeam(event.player)
//        }
        event.quitMessage(Component.text(ChatColor.YELLOW.toString()+"${event.player.name} has left (${ChatColor.AQUA}${Bukkit.getServer().onlinePlayers.size-1}${ChatColor.YELLOW}/${ChatColor.AQUA}8${ChatColor.YELLOW})!"))

    }
}