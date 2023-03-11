package com.akshardesai.timewars.team

import org.bukkit.ChatColor
import org.bukkit.Location

class GameTeam(val name: String, color: ChatColor, val spawnLocation: Location, val bedLocation: Location?=null) {
     val color: Any = color
        get () {
            return field.toString()
        }
    val trueColor = color
    val hasBed = false
    val bedLoc = bedLocation
}