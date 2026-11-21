package com.alexriley.me.hhSignTeleport.hooks

import com.alexriley.me.hhSignTeleport.HHST
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

    fun teleportPlayerToSpawn(player: Player) {
        api.spawn.thenAccept { optional ->
            optional.ifPresentOrElse({ spawn ->
                api.teleportBuilder()
                .teleporter(api.adaptUser(player))
                .target(spawn)
                .buildAndComplete(true);
            }, {
                player.sendRichMessage("<red>Spawn was not found");
            })
        }
    }

    fun teleportPlayerToBack(player: Player) {
        api.getUserLastPosition(api.adaptUser(player)).thenAccept { optional ->
            optional.ifPresentOrElse({ lastPosition ->
                api.teleportBuilder()
                .teleporter(api.adaptUser(player))
                .target(lastPosition)
                .buildAndComplete(true);
            }, {
                player.sendRichMessage("<red>You do not have a previous back position")
            })
        }
    }

    fun teleportPlayerToHome(player: Player, homeName: String) {
        api.getHome(api.adaptUser(player), homeName).thenAccept { optional ->
            if (optional.isEmpty) {
                player.sendRichMessage("<red>Home '$homeName' was not found");
                return@thenAccept;
            }

            val home = optional.get()
            api.teleportBuilder()
            .teleporter(api.adaptUser(player))
            .target(home)
            .buildAndComplete(true);
        }
    }

    fun teleportPlayerToRTP(player: Player) {
        api.randomlyTeleportPlayer(api.adaptUser(player), true);
    }
}