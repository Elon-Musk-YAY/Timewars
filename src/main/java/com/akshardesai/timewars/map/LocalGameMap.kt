package com.akshardesai.timewars.map

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File
import java.io.IOException

class LocalGameMap(worldFolder: File?, worldName: String, loadOnInit: Boolean) : GameMap {
    private var activeWorldFolder: File? = null
    private val sourceWorldFolder: File
    override var world: World? = null
        private set

    init {
        sourceWorldFolder = File(worldFolder, worldName)
        if (loadOnInit) load()
    }

    override val isLoaded: Boolean
        get() = world != null

    override fun load(): Boolean {
        if (isLoaded) return true
        activeWorldFolder = File(
            Bukkit.getWorldContainer().parentFile,  // Root server folder
            sourceWorldFolder.name + "_active_game"
        )
        try {
            FileUtil.copy(sourceWorldFolder, activeWorldFolder!!)
        } catch (e: IOException) {
            Bukkit.getLogger().severe("Failed to load GameMap from source folder $sourceWorldFolder")
            return false
        }
        world = Bukkit.createWorld(WorldCreator(activeWorldFolder!!.name))
        if (world != null) world!!.isAutoSave = false
        return isLoaded
    }

    override fun unload() {
        if (world != null) {
            Bukkit.unloadWorld(world!!, false)
        }
        if (activeWorldFolder != null) {
            delete(activeWorldFolder!!)
        }
        world = null
        activeWorldFolder = null
    }

    override fun restoreFromSource(): Boolean {
        unload()
        return load()
    }

    private fun delete(file: File) {
        if (file.isDirectory) {
            val files = file.listFiles() ?: return
            for (child in files) {
                delete(child)
            }
        }
        if (!file.delete()) {
            Bukkit.getLogger().severe("Could not delete temp file.")
        }
    }
}