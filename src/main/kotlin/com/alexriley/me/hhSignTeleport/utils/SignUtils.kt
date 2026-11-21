package com.alexriley.me.hhSignTeleport.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.block.sign.SignSide

object SignUtils {

    private val plainSerializer = PlainTextComponentSerializer.plainText()

    fun parseTeleportType(component: Component?): TeleportType? {
        if (component == null) return null
        return TeleportType.fromIdentifier(plainSerializer.serialize(component).trim())
    }

    fun toPlainText(component: Component): String {
        return plainSerializer.serialize(component)
    }

    fun colorLine(signSide: SignSide, lineIndex: Int, color: TextColor) {
        if (lineIndex !in 0..3) return
        val currentComponent = signSide.line(lineIndex)
        signSide.line(lineIndex, currentComponent.color(color))
    }
}