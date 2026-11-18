package com.alexriley.me.hhSignTeleport.listeners

import com.alexriley.me.hhSignTeleport.HHST
import net.kyori.adventure.text.Component
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
        if (!plugin.playerDataManager.getIgnoreSetting(event.player.uniqueId)) {
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

        // Hint msg
        if (!plugin.playerDataManager.getIgnoreSetting(event.player.uniqueId)) {
            if (blockState !is Sign) { return; }

            // Todo: check both sides and mirror them on save to avoid this weird side condition
            val sign = event.block.state as Sign;
            val brokenSide = sign.getTargetSide(event.player);

            val firstLine = brokenSide.line(0);
            val firstLineExpected = Component.text("[hhw]");

            // Not a warp sign
            if (!firstLine.equals(firstLineExpected)) { return; }

            player.sendPlainMessage("HHST sign broken!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        val sign = event.block.state as Sign;

        val eventSide = event.side;
        val otherSide = if (eventSide == Side.FRONT) Side.BACK else Side.FRONT;

        val otherSignSide = sign.getSide(otherSide);
        val modifiedSignSide = sign.getSide(eventSide);

        val firstLine = event.line(0);
        val firstLineExpected = Component.text("[hhw]");

        // Check if it's a warp
        if (firstLine?.equals(firstLineExpected) ?: return) {
            // Check if the warp is valid
            // [0] - [hhw] - already checked
            // [1] - warp name - check via HH API
            // [2] - descriptors
            // [3] - descriptors

            // Mirror to the other side
            event.lines().forEachIndexed { index, component ->
                modifiedSignSide.line(index, component);
                otherSignSide.line(index, component);
            }

            // Update the block
            sign.update();
            event.isCancelled = true;
        }
    }
}