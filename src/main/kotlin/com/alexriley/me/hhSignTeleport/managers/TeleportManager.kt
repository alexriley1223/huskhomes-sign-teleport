package com.alexriley.me.hhSignTeleport.managers

import com.alexriley.me.hhSignTeleport.HHST
import com.alexriley.me.hhSignTeleport.utils.SignUtils
import com.alexriley.me.hhSignTeleport.utils.TeleportType
import org.bukkit.block.Sign
import org.bukkit.entity.Player

class TeleportManager(private val plugin: HHST) {
    fun handle(type: TeleportType, sign: Sign, player: Player) {
        when (type) {
            TeleportType.WARP -> handleWarp(sign, player)
            TeleportType.SPAWN -> handleSpawn(sign, player)
            TeleportType.BACK -> handleBack(sign, player)
            TeleportType.HOME -> handleHome(sign, player)
            TeleportType.RTP -> handleRTP(sign, player)
        }
    }

    private fun handleWarp(sign: Sign, player: Player) {
        val clickedSide = sign.getTargetSide(player);
        val warpName = SignUtils.toPlainText(clickedSide.line(1));

        plugin.huskHomesHook?.teleportPlayerToWarp(player, warpName);
    }

    private fun handleSpawn(sign: Sign, player: Player) {
        plugin.huskHomesHook?.teleportPlayerToSpawn(player);
    }

    private fun handleBack(sign: Sign, player: Player) {
        plugin.huskHomesHook?.teleportPlayerToBack(player);
    }

    private fun handleHome(sign: Sign, player: Player) {
        val clickedSide = sign.getTargetSide(player);
        val warpName = SignUtils.toPlainText(clickedSide.line(1));

        plugin.huskHomesHook?.teleportPlayerToHome(player, warpName);
    }

    private fun handleRTP(sign: Sign, player: Player) {

        plugin.huskHomesHook?.teleportPlayerToRTP(player);
    }
}