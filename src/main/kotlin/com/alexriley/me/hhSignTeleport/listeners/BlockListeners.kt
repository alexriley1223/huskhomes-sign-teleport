package com.alexriley.me.hhSignTeleport.listeners

import com.alexriley.me.hhSignTeleport.HHST
import com.alexriley.me.hhSignTeleport.permissions.HHSTPermission
import com.alexriley.me.hhSignTeleport.utils.SignUtils
import com.alexriley.me.hhSignTeleport.utils.SignUtils.parseTeleportType
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
                player.sendRichMessage("<gold>Use [hhw,hhspawn,etc.] to create an HHST sign.")
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

        // Check if valid identifier
        val type = parseTeleportType(brokenSide.line(0)) ?: return

        if (!player.hasPermission(HHSTPermission.SIGN_REMOVE.node)) {
            event.isCancelled = true;
            player.sendRichMessage("<red>You don't have permission to break a HHST sign!")
        } else {
            // Hint msg
            if (!plugin.playerDataManager.getIgnoreSetting(player.uniqueId)) {
                player.sendRichMessage("<gold>HHST sign broken!");
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

        // Check if valid identifier
        val type = parseTeleportType(event.line(0)) ?: return

        event.isCancelled = true;

        if (event.player.hasPermission(HHSTPermission.SIGN_CREATE.node)) {
            // Check if the warp is valid
            // [0] - [identifier]
            // [1] - (optional) warp name
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

            // Log sign
//            val existingSignEntry = plugin.playerDataManager.getWarpSign(event.player.uniqueId, event.block.x, event.block.y, event.block.z);
//            if (existingSignEntry == null) {
//                plugin.slF4JLogger.info("Attempting to log sign at $event.block.x, $event.block.y, $event.block.z");
//                plugin.playerDataManager.addWarpSign(event.player.uniqueId, event.block.x, event.block.y, event.block.z, true);
//            }
        } else {
            event.player.sendRichMessage("<red>You do not have permission to create a HHST sign!");
            modifiedSignSide.line(0, Component.text(""));
            sign.update();
        }
    }
}