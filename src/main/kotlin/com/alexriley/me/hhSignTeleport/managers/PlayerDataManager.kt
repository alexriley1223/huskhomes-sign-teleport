package com.alexriley.me.hhSignTeleport.managers

import com.alexriley.me.hhSignTeleport.HHST
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.UUID
import kotlin.collections.get

class PlayerDataManager(private val plugin: HHST) {

    private val file = File(plugin.dataFolder, "playerdata.yml")
    private var config: YamlConfiguration

    data class SavedSignObject(
        val x: Double,
        val y: Double,
        val z: Double,
        val active: Boolean,
    )

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
    fun addWarpSign(uuid: UUID, x: Double, y: Double, z: Double, active: Boolean) {
        val objPath = "players.$uuid.signs";

        val signs = config.getMapList(objPath).toMutableList();
        val newSign = mapOf(
            "x" to x,
            "y" to y,
            "z" to z,
            "active" to active
        );

        signs.add(newSign);
        config.set(objPath, signs);
        config.save(file);
    }

    // Getters
    fun getIgnoreSetting(uuid: UUID?): Boolean {
        return config.getBoolean("players.$uuid.ignore")
    }
    fun getWarpSigns(uuid: UUID): List<SavedSignObject> {
        val objPath = "players.$uuid.signs";

        return config.getMapList(objPath).mapNotNull { map ->
            val x = (map["x"] as? Number)?.toDouble() ?: return@mapNotNull null
            val y = (map["y"] as? Number)?.toDouble() ?: return@mapNotNull null
            val z = (map["z"] as? Number)?.toDouble() ?: return@mapNotNull null
            val active = map["active"] as? Boolean ?: false

            SavedSignObject(x, y, z, active);
        }
    }
    fun getWarpSign(uuid: UUID, x: Double, y: Double, z: Double): SavedSignObject? {
        val objPath = "players.$uuid.signs";

        val match = config.getMapList(objPath).firstOrNull { map ->
            val objX = (map["x"] as? Number)?.toDouble()
            val objY = (map["y"] as? Number)?.toDouble()
            val objZ = (map["z"] as? Number)?.toDouble()

            objX == x && objY == y && objZ == z
        } ?: return null

        val active = match["active"] as? Boolean ?: false;

        return SavedSignObject(x, y, z, active);
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