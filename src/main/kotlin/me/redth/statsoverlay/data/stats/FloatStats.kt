package me.redth.statsoverlay.data.stats

import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.PlayerResponse
import net.minecraft.util.MathHelper

abstract class FloatStats(
    override val title: String,
    private val valueColoredRed: Float
) : StatsType {
    abstract fun getStats(stats: PlayerResponse?): Float?

    override fun format(stats: PlayerResponse?): ColoredText {
        val value = getStats(stats)
        value ?: return ColoredText.UNKNOWN
        val degree = (valueColoredRed - value) / valueColoredRed * 0.5f
        return ColoredText(
            "%.2f".format(value),
            MathHelper.hsvToRGB(degree.coerceAtLeast(0f), 0.7f, 1f)
        )
    }

    override fun compareStats(o1: PlayerResponse, o2: PlayerResponse): Int {
        val value1 = getStats(o1) ?: 0f
        val value2 = getStats(o2) ?: 0f
        return value1.compareTo(value2)
    }
}