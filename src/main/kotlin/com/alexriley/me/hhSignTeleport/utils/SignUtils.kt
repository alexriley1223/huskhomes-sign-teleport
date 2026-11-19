package com.alexriley.me.hhSignTeleport.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.block.sign.SignSide

object SignUtils {

    private val plainSerializer = PlainTextComponentSerializer.plainText()

    // Plaintext matching ignores text modifiers
    fun isLineMatching(
        component: Component?,
        expectedText: String,
        ignoreCase: Boolean = true
    ): Boolean {
        if (component == null) return false
        val plainText = plainSerializer.serialize(component).trim()
        return plainText.equals(expectedText, ignoreCase = ignoreCase)
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