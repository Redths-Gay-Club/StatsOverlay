package me.redth.statsoverlay.data

import cc.polyfrost.oneconfig.utils.dsl.mc

data class ColoredText(val text: String, val color: Int = 0xFFFFFFFF.toInt()) {
    val width = mc.fontRendererObj.getStringWidth(text)

    companion object {
        val UNKNOWN = ColoredText("?", 0xFFAAAAAA.toInt())
        val NICKED = ColoredText("§c[NICKED] §r")

        operator fun ColoredText?.plus(other: ColoredText) = ColoredText(this?.text.orEmpty() + other.text, this?.color ?: other.color)
        operator fun ColoredText?.plus(other: String) = ColoredText(this?.text.orEmpty() + other, this?.color ?: 0xFFFFFFFF.toInt())
    }
}
