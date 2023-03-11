package com.akshardesai.timewars.team

import com.akshardesai.timewars.TimeWars
import org.bukkit.ChatColor
import org.bukkit.Location


class Teams {
    companion object {
        var allTeams = listOf(
            GameTeam("WHITE", ChatColor.WHITE, Location(TimeWars.map.world, -80.0, 89.0, 46.0,180f,0f).add(0.5,1.0,0.5)),
            GameTeam("GRAY", ChatColor.DARK_GRAY, Location(TimeWars.map.world, -46.0, 89.0, -80.0,-90f,0f).add(0.5,1.0,0.5)),
            GameTeam("RED", ChatColor.RED, Location(TimeWars.map.world, 46.0, 89.0, -80.0,90f,0f).add(0.5,1.0,0.5)),
            GameTeam("BLUE", ChatColor.BLUE, Location(TimeWars.map.world, 80.0, 89.0, -46.0).add(0.5,1.0,0.5)),
            GameTeam("GREEN", ChatColor.GREEN, Location(TimeWars.map.world, 80.0, 89.0, 46.0,180f,0f).add(0.5,1.0,0.5)),
            GameTeam("YELLOW", ChatColor.YELLOW, Location(TimeWars.map.world, 46.0, 89.0, 80.0,90f,0f).add(0.5,1.0,0.5)),
            GameTeam("CYAN", ChatColor.DARK_AQUA, Location(TimeWars.map.world, -46.0, 89.0, 80.0,-90f,0f).add(0.5,1.0,0.5)),
            GameTeam("PINK", ChatColor.LIGHT_PURPLE, Location(TimeWars.map.world, -80.0, 89.0, -46.0).add(0.5,1.0,0.5))
        )
    }
}