@file:Suppress("DEPRECATION")

package com.akshardesai.timewars.listeners

import com.akshardesai.timewars.team.TeamManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.ChatColor
import org.bukkit.event.player.AsyncPlayerChatEvent
import net.kyori.adventure.text.TextComponent
class ChatMessageListener : Listener {
    @EventHandler
    private fun chatMessageFormatter(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (TeamManager.getTeam(player) != null) {
            event.format =
                ChatColor.DARK_GRAY.toString() + "[" + TeamManager.getTeamColor(player) + TeamManager.getTeamName(player) + ChatColor.DARK_GRAY.toString() + "] " + TeamManager.getTeamColor(
                    player
                ) + (player.displayName() as TextComponent).content() + ChatColor.GRAY.toString() + ": " + ChatColor.WHITE.toString() + event.message
        } else {
            event.format = ChatColor.WHITE.toString() + (player.displayName() as TextComponent).content() + ": " + event.message
        }
    }
}