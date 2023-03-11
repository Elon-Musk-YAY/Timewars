
package com.akshardesai.timewars

import com.akshardesai.timewars.commands.*
import com.akshardesai.timewars.listeners.*
import com.akshardesai.timewars.map.LocalGameMap
import com.akshardesai.timewars.team.TeamManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.DisplaySlot
import java.io.File
import java.time.Duration
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TimeWars : JavaPlugin() {
    companion object {
        var years: Int = 0
        var gameStarted = false
        val map_time = HashMap<String, Long>()
        var instance : TimeWars? = null
        var diamondCD = 10
        var emeraldCD = 20
        lateinit var map: LocalGameMap
        val invisPlayers = HashMap<Player,BukkitTask>()
        val combatTags = HashMap<Player,Player?>()
        val combatTagEvents = HashMap<Player,BukkitTask>()
        val killCount = HashMap<Player,Int>()
        val finalKillCount = HashMap<Player,Int>()
        lateinit var diamondHolograms: List<ArmorStand>
        lateinit var emeraldHolograms: List<ArmorStand>
        var upgradeHolograms: ArrayList<ArmorStand> = arrayListOf()

        fun setupPlayer(player: Player) {
            val manager = Bukkit.getScoreboardManager()
            val board = manager.newScoreboard
            val team = board.registerNewTeam("health")
            team.displayName(Component.text("Health"))
            val health = board.registerNewObjective("health", "health",Component.text("Health"))
            health.displaySlot = DisplaySlot.BELOW_NAME
            health.displayName(Component.text("${ChatColor.RED}♥"))
            val healthList = board.registerNewObjective("healthList", "health",Component.text("Healthlist"))
            healthList.displaySlot = DisplaySlot.PLAYER_LIST


            for (p: Player in Bukkit.getOnlinePlayers()) {
                team.addPlayer(p)
            }
            player.sendPlayerListHeader(Component.text("${ChatColor.GREEN}${ChatColor.BOLD}TIME WARS v1.0"))
            val mainSc = board.registerNewObjective("mainSc","dummy",Component.text("  ${ChatColor.RED}${ChatColor.BOLD}TIME WARS  ").decoration(TextDecoration.BOLD,true))
            mainSc.displaySlot = DisplaySlot.SIDEBAR
            mainSc.getScore("  ").score = 14
            mainSc.getScore("${ChatColor.RED}R ${ChatColor.RESET}Red:${ChatColor.RED} ✗ ${if (ChatColor.RED == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 13
            mainSc.getScore("${ChatColor.BLUE}B ${ChatColor.RESET}Blue:${ChatColor.GREEN} ✓ ${if (ChatColor.BLUE == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 12
            mainSc.getScore("${ChatColor.GREEN}G ${ChatColor.RESET}Green:${ChatColor.GREEN} ✓ ${if (ChatColor.GREEN == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 11
            mainSc.getScore("${ChatColor.YELLOW}Y ${ChatColor.RESET}Yellow:${ChatColor.GREEN} ✓ ${if (ChatColor.YELLOW == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 10
            mainSc.getScore("${ChatColor.DARK_AQUA}C ${ChatColor.RESET}Cyan:${ChatColor.GREEN} ✓ ${if (ChatColor.DARK_AQUA == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 9
            mainSc.getScore("${ChatColor.WHITE}W ${ChatColor.RESET}White:${ChatColor.GREEN} ✓ ${if (ChatColor.WHITE == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 8
            mainSc.getScore("${ChatColor.LIGHT_PURPLE}P ${ChatColor.RESET}Pink:${ChatColor.GREEN} ✓ ${if (ChatColor.LIGHT_PURPLE == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 7
            mainSc.getScore("${ChatColor.DARK_GRAY}G ${ChatColor.RESET}Gray:${ChatColor.GREEN} ✓ ${if (ChatColor.DARK_GRAY == TeamManager.getTeam(player)?.trueColor) "${ChatColor.GRAY}YOU" else ""}").score = 6
            mainSc.getScore(" ").score = 5
            mainSc.getScore("Kills:${ChatColor.GREEN} 0").score = 4
            mainSc.getScore("Final Kills:${ChatColor.GREEN} 0").score = 3
            mainSc.getScore("Beds Broken:${ChatColor.GREEN} 0").score = 2
            mainSc.getScore("        ").score = 1
            mainSc.getScore("${ChatColor.YELLOW}173.92.206.157").score = 0
            player.scoreboard = board
            player.health = player.health - 0.0001
            player.playerListName(Component.text("${TeamManager.getTeamColor(player)}${ChatColor.BOLD}${TeamManager.getTeamName(player)[0]}${ChatColor.RESET}${TeamManager.getTeamColor(player)} ${player.name}"))
            assignGameTeam(player)
        }
        private fun assignGameTeam(player: Player) {
            val manager = Bukkit.getServer().scoreboardManager
            val board = manager.mainScoreboard
            val playerTeamSpecific = board.registerNewTeam(TeamManager.getTeamName(player))
            playerTeamSpecific.displayName(Component.text("Player Specific Team"))
            playerTeamSpecific.prefix(Component.text("${ChatColor.BOLD}${TeamManager.getTeamColor(player)}${TeamManager.getTeamName(player)[0]} "))
            playerTeamSpecific.addPlayer(player)

            @Suppress("DEPRECATION")
            playerTeamSpecific.color = TeamManager.getTeam(player)?.trueColor as ChatColor
        }
        fun addKill(player: Player, killMessage: Component) {
            if (TeamManager.getTeam(player)!!.hasBed) {

                if (killCount[player.killer as Player] == null) {
                    killCount[player.killer as Player] = 1
                } else {
//                println(TimeWars.killCount[player.killer as Player])
                    killCount[player.killer as Player] = killCount[player.killer as Player]!! + 1
//                println(TimeWars.killCount[player.killer as Player])
                }
                val killerBoard = (player.killer as Player).scoreboard
                killerBoard.resetScores("Kills:${ChatColor.GREEN} ${(killCount[player.killer as Player]!! - 1)}")
                killerBoard.getObjective("mainSc")
                    ?.getScore("Kills:${ChatColor.GREEN} ${killCount[player.killer as Player]}")?.score = 3
                Bukkit.getServer().sendMessage(killMessage)
            } else {
                if (finalKillCount[player.killer as Player] == null) {
                    finalKillCount[player.killer as Player] = 1
                } else {
//                println(TimeWars.finalKillCount[player.killer as Player])
                    finalKillCount[player.killer as Player] = finalKillCount[player.killer as Player]!! + 1
//                println(TimeWars.finalKillCount[player.killer as Player])
                }
                val killerBoard = (player.killer as Player).scoreboard
                killerBoard.resetScores("Final Kills:${ChatColor.GREEN} ${(finalKillCount[player.killer as Player]!! - 1)}")
                killerBoard.getObjective("mainSc")
                    ?.getScore("Final Kills:${ChatColor.GREEN} ${finalKillCount[player.killer as Player]}")?.score = 3
                Bukkit.getServer().sendMessage(killMessage.append(Component.text("${ChatColor.BOLD}${ChatColor.AQUA} FINAL KILL")))
            }
        }
    }
        
    private lateinit var upgradeShopLocations: List<Location>


    override fun onEnable() {
        // Plugin startup logic
        instance = this
        dataFolder.mkdirs()
        val gameMapsFolder = File(dataFolder, "gameMaps")
        if (!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs()
        }
        map = LocalGameMap(gameMapsFolder,"Crogorm",true)
        upgradeShopLocations = listOf(Location(map.world!!, -46.0, 90.0, 85.0),Location(map.world!!,-85.0,90.0,-46.0),Location(map.world!!,-75.0,90.0,46.0),Location(map.world!!,46.0,90.0,-85.0),Location(map.world!!,75.0,90.0,-46.0),Location(map.world!!,85.0,90.0,46.0),Location(map.world!!,-46.0,90.0,-75.0),Location(map.world!!,46.0,90.0,75.0))
        println("Timewars is active!")
        getCommand("fly")?.setExecutor(FlyCommand())
        getCommand("gameToggle")?.setExecutor(StartGameCommand())
        getCommand("teamChange")?.setExecutor(GetTeamCommand())
        getCommand("play")?.setExecutor(WarpCommand())
        getCommand("lobby")?.setExecutor(LobbyCommand())
        getCommand("server")?.setExecutor(TestCommand())
        server.pluginManager.registerEvents(PlayerInteractListener(), this)
        server.pluginManager.registerEvents(BlockPlacedListener(), this)
        server.pluginManager.registerEvents(BlockDestroyedListener(), this)
        server.pluginManager.registerEvents(BlockPhysicsListener(), this)
        server.pluginManager.registerEvents(ChatMessageListener(),this)
        server.pluginManager.registerEvents(PlayerJoinLeaveListener(),this)
        server.pluginManager.registerEvents(PotionDrinkListener(),this)
        server.pluginManager.registerEvents(PlayerDamageListener(),this)
        server.pluginManager.registerEvents(TNTExplodeListener(),this)
        for (entity: Entity in map.world!!.entities) {
            if (entity.type != EntityType.PLAYER) {
                entity.remove()
            }
        }
        for (teamIn in Bukkit.getScoreboardManager().mainScoreboard.teams) {
            teamIn.unregister()
        }
        for (player: Player in server.onlinePlayers) {
            for (team in player.scoreboard.teams) {
                team.unregister()
            }
            for (objective in player.scoreboard.objectives) {
                objective.unregister()
            }
        }


        for (player: Player in server.onlinePlayers) {
            TeamManager.setTeam(player)
            setupPlayer(player)

        }





       setupSpawners()
       setupShopHolograms()
       spawnUpgradeShop()

        this.server.scheduler
            .scheduleSyncRepeatingTask(this, {
                for (player: Player in server.onlinePlayers) {
                    if (player.location.y <= -6.0) {
                        player.gameMode = GameMode.SPECTATOR
                        println(combatTags[player])
                        if (combatTags[player] != null) {
                            combatTagEvents[player]?.cancel()
                            addKill(player,Component.text("${TeamManager.getTeamColor(player)}${player.name}${ChatColor.GRAY} was knocked into the void by ${TeamManager.getTeamColor(combatTags[player] as Player)}${(combatTags[player] as Player).name}${ChatColor.GRAY}."))
                            combatTags[player] = null
                        }
                        else {
                            server.sendMessage(Component.text("${TeamManager.getTeamColor(player)}${player.name}${ChatColor.GRAY} fell into the void.").append(Component.text(if (TeamManager.getTeam(player)!!.hasBed) "" else "${ChatColor.BOLD}${ChatColor.AQUA} FINAL KILL").decoration(TextDecoration.BOLD,true)))
                        }
                        player.teleport(Location(map.world!!,0.5,172.0,0.5))
                        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(1L),Duration.ofSeconds(4L),Duration.ofSeconds(1L)))
                        player.sendTitlePart(TitlePart.TITLE,Component.text(ChatColor.RED.toString()+"YOU DIED!"))
                        for (effect in player.activePotionEffects) {
                            player.removePotionEffect(effect.type)
                        }
                        var respawnTime = 6
                        object: BukkitRunnable() {
                            override fun run() {
                                respawnTime--
                                if (respawnTime == 0) {
                                    player.gameMode = GameMode.SURVIVAL
                                    this.cancel()
                                    player.teleport(TeamManager.getTeam(player)?.spawnLocation as Location)
                                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(1L),Duration.ofSeconds(1L),Duration.ofSeconds(1L)))
                                    player.sendTitlePart(TitlePart.TITLE,Component.text(ChatColor.GREEN.toString()+"RESPAWNED!"))
                                    player.sendTitlePart(TitlePart.SUBTITLE,Component.text(""))
                                    player.sendMessage(Component.text(ChatColor.YELLOW.toString() + "You have respawned!"))
                                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(1L),Duration.ofSeconds(4L),Duration.ofSeconds(1L)))



                                } else {
                                    player.sendMessage(Component.text(ChatColor.YELLOW.toString() + "You will respawn in ${ChatColor.RED}$respawnTime${ChatColor.YELLOW} second${if (respawnTime == 1) "" else "s"}!"))
                                    player.sendTitlePart(
                                        TitlePart.SUBTITLE,
                                        Component.text(ChatColor.YELLOW.toString() + "You will respawn in ${ChatColor.RED}$respawnTime${ChatColor.YELLOW} second${if (respawnTime == 1) "" else "s"}!")
                                    )
                                }
                            }
                        }.runTaskTimer(this,0L,20L)



                    }
                }
            }, 0L, 1L)

        // Time Travel
        this.server.scheduler
            .scheduleSyncRepeatingTask(this, {
                if (!gameStarted) return@scheduleSyncRepeatingTask
                if (years < 3050) {
                    for (w in server.worlds) {
                        w.time = map_time[w.name]!! + 127
                        map_time.remove(w.name)
                        map_time[w.name] = w.time
                    }
                }
            }, 0L, 1L)
        // Years actionbar
        this.server.scheduler
            .scheduleSyncRepeatingTask(this, {
                if (!gameStarted) return@scheduleSyncRepeatingTask
                if (years < 3050) {
                    years += 1
                }
                when (years) {
                    2700 -> {
                        for (player in server.onlinePlayers) {
                            player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "WARNING: THE SHOP HAS REACHED THE FINAL UPGRADE")
                            player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 2f)

                        }
                    }
                    1900 -> {
                        for (player in server.onlinePlayers) {
                            player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "WARNING: THE SHOP HAS BEEN UPGRADED")
                            player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f)

                        }
                    }
                    1200 -> {
                        for (player in server.onlinePlayers) {
                            player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "WARNING: THE SHOP HAS BEEN UPGRADED")
                            player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f)

                        }
                    }
                    800 -> {
                        for (player in server.onlinePlayers) {
                            player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "WARNING: THE UPGRADES SHOP HAS BEEN INVENTED")
                            player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 0.5f)
                        }
                        for (holo:ArmorStand in upgradeHolograms) {
                            holo.customName(Component.text(ChatColor.YELLOW.toString()+"RIGHT CLICK"))
                        }
                        for (location: Location in upgradeShopLocations) {
                            location.block.type = Material.POLISHED_DEEPSLATE
                        }
                    }
                    500 -> {
                        for (player in server.onlinePlayers) {
                            player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "WARNING: THE SHOP HAS BEEN UPGRADED")
                            player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f)
                            spawnUpgradeShop()

                        }
                    }
                }
                for (player in server.onlinePlayers) {
                    player.sendActionBar(Component.text("YEAR: $years", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false))
                }


            }, 0L, 4L)
    }
    private fun setupSpawners() {
        // Setup Diamond Holograms
        val armor1 : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, -100.0, 88.0, 0.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        val armor2 : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, 0.0, 88.0, 100.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        val armor3 : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, 100.0, 88.0, 0.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        val armor4 : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, 0.0, 88.0, -100.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        diamondHolograms = listOf(armor1,armor2,armor3,armor4)
        for (hologram: ArmorStand in diamondHolograms) {
            hologram.isVisible = false
            hologram.canPickupItems = false
            hologram.setGravity(false)
            hologram.isCustomNameVisible = true
            val topPiece = (map.world!!.spawnEntity(hologram.location.add(0.0,0.3,0.0),EntityType.ARMOR_STAND)) as ArmorStand
            topPiece.isVisible = false
            topPiece.canPickupItems = false
            topPiece.setGravity(false)
            topPiece.isCustomNameVisible = true
            topPiece.customName(Component.text(ChatColor.AQUA.toString()+"Diamond Generator"))
            hologram.customName(Component.text(ChatColor.GOLD.toString()+"Spawning in: "+ChatColor.AQUA.toString()+"$diamondCD"+ChatColor.GOLD.toString()+" seconds"))
        }

        val armor1e : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, 17.0, 93.0, 8.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        val armor2e : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, -8.0, 93.0, 17.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        val armor3e : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, -17.0, 93.0, -8.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        val armor4e : ArmorStand = (map.world!!.spawnEntity(Location(map.world!!, 8.0, 93.0, -17.0).add(0.5, 2.2, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
        emeraldHolograms = listOf(armor1e,armor2e,armor3e,armor4e)
        for (hologram: ArmorStand in emeraldHolograms) {
            hologram.isVisible = false
            hologram.canPickupItems = false
            hologram.setGravity(false)
            hologram.isCustomNameVisible = true
            val topPiece = (map.world!!.spawnEntity(hologram.location.add(0.0,0.3,0.0),EntityType.ARMOR_STAND)) as ArmorStand
            topPiece.isVisible = false
            topPiece.canPickupItems = false
            topPiece.setGravity(false)
            topPiece.isCustomNameVisible = true
            topPiece.customName(Component.text(ChatColor.GREEN.toString()+"Emerald Generator"))
            hologram.customName(Component.text(ChatColor.GOLD.toString()+"Spawning in: "+ChatColor.GREEN.toString()+"$emeraldCD"+ChatColor.GOLD.toString()+" seconds"))
        }
    }
    private fun spawnUpgradeShop() {

        for (location: Location in upgradeShopLocations) {
            val holoTop : ArmorStand = (map.world!!.spawnEntity(location.clone().add(0.5, -0.4, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
            val holoBottom : ArmorStand = (map.world!!.spawnEntity(location.clone().add(0.5, -0.7, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
            holoTop.isVisible = false
            holoTop.canPickupItems = false
            holoTop.setGravity(false)
            holoTop.isCustomNameVisible = true
            holoBottom.isVisible = false
            holoBottom.canPickupItems = false
            holoBottom.setGravity(false)
            holoBottom.isCustomNameVisible = true
            holoTop.customName(Component.text(ChatColor.RED.toString()+"Upgrades"))
            holoBottom.customName(Component.text(ChatColor.YELLOW.toString()+"Invented in year 800"))
            upgradeHolograms.add(holoBottom)

        }
    }
    private fun setupShopHolograms () {
        val locations : List<Location> = listOf(Location(map.world!!,-85.0,90.0,46.0),Location(map.world!!,-75.0,90.0,-46.0),Location(map.world!!,-46.0,90.0,-85.0),Location(map.world!!,46.0,90.0,-75.0),Location(map.world!!,46.0,90.0,85.0),Location(map.world!!,75.0,90.0,46.0),Location(map.world!!,85.0,90.0,-46.0),Location(map.world!!,-46.0,90.0,75.0))
        for (loc:Location in locations ) {
                var tempLoc = Location(map.world!!,loc.x,loc.y,loc.z)

                val holoTop : ArmorStand = (map.world!!.spawnEntity(tempLoc.add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
                tempLoc = Location(map.world!!,loc.x,loc.y,loc.z)
                val holoBottom : ArmorStand = (map.world!!.spawnEntity(tempLoc.add(0.5, -0.8, 0.5), EntityType.ARMOR_STAND)) as ArmorStand
                holoTop.isVisible = false
                holoTop.canPickupItems = false
                holoTop.setGravity(false)
                holoTop.isCustomNameVisible = true
                holoBottom.isVisible = false
                holoBottom.canPickupItems = false
                holoBottom.setGravity(false)
                holoBottom.isCustomNameVisible = true
                holoTop.customName(Component.text(ChatColor.RED.toString()+"Shop"))
                holoBottom.customName(Component.text(ChatColor.YELLOW.toString()+"RIGHT CLICK"))
            }
        }






    override fun onDisable() {
        map.unload()
        instance = null
        // Plugin shutdown logic
    }
}