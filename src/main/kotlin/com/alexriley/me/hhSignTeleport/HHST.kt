package com.alexriley.me.hhSignTeleport

import com.alexriley.me.hhSignTeleport.listeners.BlockListeners
import org.bukkit.plugin.java.JavaPlugin

class HHST : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()
        registerListeners()
        logger.info("Plugin enabled on Minecraft 26.3!")
    }

    private fun registerListeners() {
        val listeners = listOf(
            BlockListeners()
        )

        listeners.forEach { listener -> server.pluginManager.registerEvents(listener, this) }
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("Plugin disabled.")
    }
}
