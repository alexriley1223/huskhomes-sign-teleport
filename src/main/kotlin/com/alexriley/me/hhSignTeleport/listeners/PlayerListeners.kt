package com.alexriley.me.hhSignTeleport.listeners

import com.alexriley.me.hhSignTeleport.HHST
import com.alexriley.me.hhSignTeleport.permissions.HHSTPermission
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

        // Check if we have a sign
        val blockState = event.clickedBlock?.state ?: return
        if (blockState !is Sign) {
            return;
        }

        // Check if warp sign
        // TODO: Handle multiple types of warps
        val sign = event.clickedBlock?.state as Sign;
        val clickedSide = sign.getTargetSide(event.player);

        val firstLine = clickedSide.line(0);
        val firstLineExpected = Component.text("[hhw]");

        // Not a warp sign
        if (firstLine != firstLineExpected) {
            return;
        }

        // Edit check
        if (event.player.isSneaking) {
            if (!event.player.hasPermission(HHSTPermission.SIGN_EDIT.node)) {
                event.isCancelled = true;
                event.player.sendPlainMessage("You don't have permission to modify a HHST sign!")
            }
        } else {
            event.isCancelled = true;

            // Use check / TP
            if (event.player.hasPermission(HHSTPermission.SIGN_USE.node)) {
                plugin.huskHomesHook?.teleportPlayerToWarp(event.player, PlainTextComponentSerializer.plainText().serialize(clickedSide.line(1)));
            } else {
                event.player.sendPlainMessage("You don't have permission to use a HHST sign!");
            }
        }
    }
}