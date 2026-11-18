package com.alexriley.me.hhSignTeleport.commands.utility

import com.alexriley.me.hhSignTeleport.HHST
import com.alexriley.me.hhSignTeleport.commands.HHSTCommand
import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

class IgnorePromptsCommand(private val plugin: HHST) : HHSTCommand {
    private val mm = MiniMessage.miniMessage()

    override fun register(registrar: Commands) {
        val node = Commands.literal("ignore-prompts")
            .executes(::addToIgnoreList)
            .build()

        registrar.register(
            node,
            "Ignores sign placement prompt for sign teleports",
            listOf("restore")
        )
    }

    private fun addToIgnoreList(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.executor as? Player
        if (player == null) {
            ctx.source.sender.sendMessage { mm.deserialize("[HHST] <red>Only players can add themselves to the HHST ignore list") }
        }

        val currentState = plugin.playerDataManager.getIgnoreSetting(player?.uniqueId)
        plugin.playerDataManager.setIgnoreSetting(player?.uniqueId, !currentState)

        if (!currentState) {
            ctx.source.sender.sendMessage { mm.deserialize("[HHST] <green>Added to the placement ignore list. Run this command again to see them again.") }
        } else {
            ctx.source.sender.sendMessage { mm.deserialize("[HHST] <yellow>Removed from the placement ignore list. You will see placement hints again.") }
        }

        return Command.SINGLE_SUCCESS
    }
}