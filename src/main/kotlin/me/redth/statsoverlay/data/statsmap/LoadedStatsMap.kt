package me.redth.statsoverlay.data.statsmap

import com.mojang.authlib.GameProfile
import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.ColoredText.Companion.plus
import me.redth.statsoverlay.data.PlayerResponse
import me.redth.statsoverlay.data.stats.StatsType
import me.redth.statsoverlay.data.stats.StatsTypes

open class LoadedStatsMap(
    override val profile: GameProfile,
    override val response: PlayerResponse,
    private val statsMap: Map<StatsType, ColoredText>
) : StatsMap {
    override fun format(statType: StatsType) = statsMap[statType] ?: ColoredText.UNKNOWN
    override val coloredName get() = statsMap[StatsTypes.BW_STARS_PREFIX] + ColoredText(formattedName)
}