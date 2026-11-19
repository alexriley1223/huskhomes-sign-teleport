package com.alexriley.me.hhSignTeleport.listeners

import com.alexriley.me.hhSignTeleport.HHST
import com.alexriley.me.hhSignTeleport.permissions.HHSTPermission
import com.alexriley.me.hhSignTeleport.utils.SignUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.SignChangeEvent

class BlockListeners(private val plugin: HHST) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player;
        val block = event.blockPlaced;
        val blockState = block.state;

        // Hint msg
        if (!plugin.playerDataManager.getIgnoreSetting(player.uniqueId)) {
            if (blockState is Sign) {
                player.sendPlainMessage("Use [hhw] to create an HHST sign.")
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player;
        val block = event.block;
        val blockState = block.state;

        if (blockState !is Sign) { return; }

        val sign = event.block.state as Sign;
        val brokenSide = sign.getTargetSide(player);

        // TODO: Add further supported sign types
        if (!SignUtils.isLineMatching(brokenSide.line(0), "[hhw]")) { return; }

        if (!player.hasPermission(HHSTPermission.SIGN_REMOVE.node)) {
            event.isCancelled = true;
            player.sendPlainMessage("You don't have permission to break a HHST sign!")
        } else {
            // Hint msg
            if (!plugin.playerDataManager.getIgnoreSetting(player.uniqueId)) {
                player.sendPlainMessage("HHST sign broken!");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        val sign = event.block.state as Sign;

        val eventSide = event.side;
        val otherSide = if (eventSide == Side.FRONT) Side.BACK else Side.FRONT;

        val otherSignSide = sign.getSide(otherSide);
        val modifiedSignSide = sign.getTargetSide(event.player);

        if (!SignUtils.isLineMatching(event.line(0), "[hhw]")) { return; }

        event.isCancelled = true;

        if (event.player.hasPermission(HHSTPermission.SIGN_CREATE.node)) {
            // Check if the warp is valid
            // [0] - [hhw] - already checked
            // [1] - warp name - check via HH API
            // [2] - descriptors
            // [3] - descriptors

            // Mirror to the other side
            event.lines().forEachIndexed { index, component ->
                // Warp Side
                modifiedSignSide.line(index, component);
                otherSignSide.line(index, component);
            }

            SignUtils.colorLine(modifiedSignSide, 0, NamedTextColor.GOLD);
            SignUtils.colorLine(otherSignSide, 0, NamedTextColor.GOLD);

            sign.update();
        } else {
            event.player.sendPlainMessage("You do not have permission to create a HHST sign!");
            modifiedSignSide.line(0, Component.text(""));
            sign.update();
        }
    }
}