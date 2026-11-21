package com.alexriley.me.hhSignTeleport.utils

enum class TeleportType(val identifier: String) {
    WARP("[hhw]"), // Warps
    SPAWN("[hhspawn]"), // Spawn
    BACK("[hhback]"), // /back previous pos
    HOME("[hhh]"), // Player home
    RTP("[hhrtp]"); // RTP

    companion object {
        private val byIdentifier = entries.associateBy { it.identifier }

        fun fromIdentifier(raw: String): TeleportType? =
            byIdentifier[raw.lowercase()]
    }
}