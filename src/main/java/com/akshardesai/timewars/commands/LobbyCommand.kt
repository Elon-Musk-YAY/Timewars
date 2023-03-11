package com.akshardesai.timewars.commands

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LobbyCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>?): Boolean {
        val world = Bukkit.getWorld("lobby")
        if (sender is Player) {
            sender.teleport(Location(world,0.5,84.0,0.5))
            sender.fallDistance = 0f
        }
        return true
    }
}