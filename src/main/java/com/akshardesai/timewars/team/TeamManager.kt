package com.akshardesai.timewars.team

import org.bukkit.entity.Player

class TeamManager {
    companion object {
        private val playerTeams = HashMap<Player, GameTeam?>()
        private val allGameTeams = Teams.allTeams.toMutableList()
        fun setTeam(player: Player) {
            val teamChoice = allGameTeams.shuffled().first()
            if (playerTeams[player]!= null) {
                playerTeams[player]?.let { allGameTeams.add(it) }
            }
            playerTeams[player] = teamChoice
            allGameTeams.remove(teamChoice)
        }
        fun removeTeam(player: Player) {
            allGameTeams.add(playerTeams[player] as GameTeam)
            playerTeams[player] = null
        }
        fun getTeam(player:Player) : GameTeam? {
            return playerTeams[player]
        }

        fun getTeamName(player: Player): String {
            return playerTeams[player]!!.name
        }
        fun getTeamColor(player: Player) : String {
            return playerTeams[player]?.color as String
        }
    }
}