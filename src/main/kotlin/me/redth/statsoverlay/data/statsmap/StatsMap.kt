package me.redth.statsoverlay.data.statsmap

import cc.polyfrost.oneconfig.utils.dsl.mc
import com.mojang.authlib.GameProfile
import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.PlayerResponse
import me.redth.statsoverlay.data.stats.StatsType
import me.redth.statsoverlay.data.stats.StatsTypes
import net.minecraft.scoreboard.ScorePlayerTeam

interface StatsMap {
    val profile: GameProfile
    val response: PlayerResponse? get() = null

    val formattedName: String
        get() {
            val name = profile.name
            val world = mc.theWorld ?: return name
            val team = world.scoreboard.getPlayersTeam(name)
            return ScorePlayerTeam.formatPlayerName(team, name)
        }

    val coloredName get() = ColoredText(formattedName)

    operator fun get(statType: StatsType): ColoredText {
        if (statType == StatsTypes.BW_STARS_PREFIX) return coloredName
        return format(statType)
    }

    fun format(statType: StatsType) = ColoredText.UNKNOWN
}