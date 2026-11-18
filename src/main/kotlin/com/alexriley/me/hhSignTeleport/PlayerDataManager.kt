package com.alexriley.me.hhSignTeleport

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.UUID

class PlayerDataManager(private val plugin: HHST) {

    private val file = File(plugin.dataFolder, "playerdata.yml")
    private var config: YamlConfiguration

    init {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }

        if (!file.exists()) {
            file.createNewFile()
        }

        config = YamlConfiguration.loadConfiguration(file)
    }

    // Setters
    fun setIgnoreSetting(uuid: UUID?, ignore: Boolean) {
        config.set("players.$uuid.ignore", ignore)
        save()
    }

    // Getters
    fun getIgnoreSetting(uuid: UUID?): Boolean {
        return config.getBoolean("players.$uuid.ignore")
    }

    // Operators
    fun save() {
        try {
            config.save(file)
        } catch (e: IOException) {
            plugin.slF4JLogger.error("Failed to save playerdata.yml.", e)
        }
    }

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}