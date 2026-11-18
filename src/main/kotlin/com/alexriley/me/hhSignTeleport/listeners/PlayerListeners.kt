package com.alexriley.me.hhSignTeleport.listeners

import com.alexriley.me.hhSignTeleport.HHST
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class PlayerListeners(private val plugin: HHST) : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        // Check for a valid sign click (interact is on a block, with primary hand, on a valid block, while not crouching)
        if (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) return;
        if (event.hand != EquipmentSlot.HAND) return;
        if (event.clickedBlock == null) return;
        if (event.player.isSneaking) return;

        // Check if we have a sign
        val blockState = event.clickedBlock?.state ?: return
        if (blockState !is Sign) {
            return;
        }

        // Check if warp sign
        val sign = event.clickedBlock?.state as Sign;
        val clickedSide = sign.getTargetSide(event.player);

        val firstLine = clickedSide.line(0);
        val firstLineExpected = Component.text("[hhw]");

        // Not a warp sign
        if (!firstLine.equals(firstLineExpected)) {
            return;
        }
        event.isCancelled = true;

        // Call HH API to TP based on 2nd line warp name. These can be nonexistant warps as HH will handle it and show the user.
        plugin.huskHomesHook?.teleportPlayerToWarp(event.player, PlainTextComponentSerializer.plainText().serialize(clickedSide.line(1)));
    }
}