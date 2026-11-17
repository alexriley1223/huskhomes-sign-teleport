package com.alexriley.me.hhSignTeleport.listeners

import org.bukkit.block.Sign
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class BlockListeners : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player;
        val block = event.blockPlaced;
        val blockState = block.state;

        if (blockState is Sign) {
            player.sendPlainMessage("Use [warp] to create an HHST sign.")
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player;
        val block = event.block;
        val blockState = block.state;

        if (blockState is Sign) {
            player.sendPlainMessage("Sign is broken");
        }
    }
}