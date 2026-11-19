package com.alexriley.me.hhSignTeleport

import com.alexriley.me.hhSignTeleport.commands.HHSTCommand
import com.alexriley.me.hhSignTeleport.commands.utility.IgnorePromptsCommand
import com.alexriley.me.hhSignTeleport.hooks.HuskHomesHook
import com.alexriley.me.hhSignTeleport.listeners.BlockListeners
import com.alexriley.me.hhSignTeleport.listeners.PlayerListeners
import com.alexriley.me.hhSignTeleport.managers.PlayerDataManager
import com.alexriley.me.hhSignTeleport.permissions.PermissionManager
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

class HHST : JavaPlugin() {

    lateinit var playerDataManager: PlayerDataManager
        private set

    var huskHomesHook: HuskHomesHook? = null
        private set

    private lateinit var permissionManager: PermissionManager

    override fun onEnable() {
        playerDataManager = PlayerDataManager(this)

        // Verify HH exists
        if (server.pluginManager.isPluginEnabled("HuskHomes")) {
            huskHomesHook = HuskHomesHook(this)
            logger.info("Hooked into HuskHomes!")
        }

        // Permissions
        permissionManager = PermissionManager(this)
        permissionManager.registerAll()

        saveDefaultConfig()
        checkConfigVersion()
        registerListeners()
        registerCommands()

        logger.info("HHST ready to go!")
    }

    override fun onDisable() {
        if (::permissionManager.isInitialized) {
            permissionManager.unregisterAll();
        }

        logger.info("Plugin disabled.")
    }

    private fun registerListeners() {
        val listeners = listOf(
            BlockListeners(this),
            PlayerListeners(this)
        )

        listeners.forEach { listener -> server.pluginManager.registerEvents(listener, this) }
    }

    private fun registerCommands() {
        val commands: List<HHSTCommand> = listOf(
            IgnorePromptsCommand(this)
        )

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val registrar = event.registrar()
            commands.forEach { it.register(registrar) }
        }
    }

    private fun checkConfigVersion() {
        val currentVersion = pluginMeta.version;
        val savedVersion = config.getString("config-version");

        if (savedVersion == null || savedVersion != currentVersion) {
            logger.info("Config version mismatch. (found: $savedVersion, expected: $currentVersion). Updating config...");

            // Any migration manual checks here

            config.options().copyDefaults(true);
            config.set("config-version", currentVersion);
            saveConfig();
        }
    }
}
