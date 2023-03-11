package com.akshardesai.timewars.commands

import com.akshardesai.timewars.team.TeamManager
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GetTeamCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.hasPermission("timewars.teamChange")) {
                TeamManager.setTeam(player)
                player.playerListName(Component.text("${TeamManager.getTeamColor(player)}${ChatColor.BOLD}${TeamManager.getTeamName(player)[0]}${ChatColor.RESET}${TeamManager.getTeamColor(player)} ${player.name}"))
                player.sendMessage("Team is now ${TeamManager.getTeamName(player)}")
            } else {
                player.sendMessage(ChatColor.RED.toString()+"You must have the timewars.teamChange permission to change teams!")
            }
        }
        return true
    }
}