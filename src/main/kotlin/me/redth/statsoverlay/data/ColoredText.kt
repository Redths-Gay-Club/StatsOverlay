package me.redth.statsoverlay.data

import cc.polyfrost.oneconfig.utils.dsl.mc

data class ColoredText(
    val text: String,
    val color: Int = 0xFFFFFFFF.toInt(),
    val alignment: Alignment = Alignment.CENTER,
) {
    val width = mc.fontRendererObj.getStringWidth(text)

    companion object {
        val UNKNOWN = ColoredText("?", 0xFFAAAAAA.toInt())
        val NICKED = ColoredText("§c[NICKED] §r")
    }
}

enum class Alignment {
    LEFT,
    CENTER,
    RIGHT,
}
