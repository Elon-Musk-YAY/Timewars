package com.akshardesai.timewars.commands

import com.akshardesai.timewars.TimeWars
import com.akshardesai.timewars.team.TeamManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>?): Boolean {
        val world = Bukkit.getWorld(TimeWars.map.world!!.name)
        if (sender is Player) {
            sender.teleport(Location(world,0.5,174.0,0.5))
            (TimeWars.map.world as World).sendMessage(Component.text(ChatColor.YELLOW.toString()+"${sender.name} has joined (${ChatColor.AQUA}${Bukkit.getServer().onlinePlayers.size}${ChatColor.YELLOW}/${ChatColor.AQUA}8${ChatColor.YELLOW})!"))
            if (TeamManager.getTeam(sender) == null) {
                for (team in sender.scoreboard.teams) {
                    team.unregister()
                }
                for (objective in sender.scoreboard.objectives) {
                    objective.unregister()
                }
                TeamManager.setTeam(sender)
                sender.fallDistance = 0f
                TimeWars.setupPlayer(sender)
            }
        }
        return true
    }
}