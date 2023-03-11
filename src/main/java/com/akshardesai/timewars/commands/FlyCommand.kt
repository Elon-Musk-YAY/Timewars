package com.akshardesai.timewars.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.hasPermission("timewars.fly")) {
                if (!player.allowFlight) {
                    player.allowFlight = true
                    player.sendMessage(ChatColor.GREEN.toString() + "You may now fly!")
                } else {
                    player.allowFlight = false
                    player.sendMessage(ChatColor.RED.toString() + "Flying has been disabled for you!")
                }
            } else {
                player.sendMessage(ChatColor.RED.toString()+"You must have the timewars.fly permission to fly!")
            }
        }
        return true
    }
}