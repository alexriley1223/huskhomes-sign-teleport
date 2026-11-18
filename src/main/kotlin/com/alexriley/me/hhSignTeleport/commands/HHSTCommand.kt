package com.alexriley.me.hhSignTeleport.commands

import io.papermc.paper.command.brigadier.Commands

interface HHSTCommand {
    fun register(registrar: Commands)
}