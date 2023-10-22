package me.redth.statsoverlay.data.stats

import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.PlayerResponse
import net.minecraft.util.MathHelper

abstract class IntStats(
    override val title: String,
    private val valueColoredRed: Int
) : StatsType {
    abstract fun getStats(stats: PlayerResponse?): Int?

    override fun format(stats: PlayerResponse?): ColoredText {
        val value = getStats(stats)
        value ?: return ColoredText.UNKNOWN
        val degree = (valueColoredRed - value) / valueColoredRed.toFloat() * 0.5f
        return ColoredText(
            "%,d".format(value),
            MathHelper.hsvToRGB(degree.coerceAtLeast(0f), 0.7f, 1f)
        )
    }

    override fun compareStats(o1: PlayerResponse, o2: PlayerResponse): Int {
        val value1 = getStats(o1) ?: 0
        val value2 = getStats(o2) ?: 0
        return value1.compareTo(value2)
    }
}