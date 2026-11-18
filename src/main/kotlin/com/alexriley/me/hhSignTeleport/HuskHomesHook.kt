package com.alexriley.me.hhSignTeleport

import net.william278.huskhomes.api.HuskHomesAPI
import org.bukkit.entity.Player

class HuskHomesHook(private val plugin: HHST) {
    private val api: HuskHomesAPI = HuskHomesAPI.getInstance()

    fun teleportPlayerToWarp(player: Player, warpName: String) {
        api.getWarp(warpName).thenAccept { optional ->
            optional.ifPresentOrElse({ warp ->
                api.teleportBuilder()
                    .teleporter(api.adaptUser(player))
                    .target(warp)
                    .buildAndComplete(true)
            }, {
                player.sendMessage("Warp `$warpName` was not found")
            })
        };
    }
}