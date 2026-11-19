package com.alexriley.me.hhSignTeleport.permissions

import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin

class PermissionManager(private val plugin: JavaPlugin) {

    fun registerAll() {
        val pm = plugin.server.pluginManager;

        // Register all base perms
        for (entry in HHSTPermission.entries) {
            if (pm.getPermission(entry.node) == null) {
                val bukkitPerm = Permission(entry.node, entry.description, entry.default);
                pm.addPermission(bukkitPerm);
            }
        }

        // Parent-child inheritance after all perms exist
        for (entry in HHSTPermission.entries) {
            val parentEntry = entry.parent ?: continue
            val parentPerm = pm.getPermission(parentEntry.node) ?: continue

            parentPerm.children[entry.node] = true
            parentPerm.recalculatePermissibles();
        }

    }

    fun unregisterAll() {
        val pm = plugin.server.pluginManager;
        for (entry in HHSTPermission.entries) {
            pm.removePermission(entry.node);
        }
    }
}