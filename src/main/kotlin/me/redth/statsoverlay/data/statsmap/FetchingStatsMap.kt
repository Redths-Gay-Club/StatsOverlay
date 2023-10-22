package me.redth.statsoverlay.data.statsmap

import cc.polyfrost.oneconfig.utils.dsl.runAsync
import com.mojang.authlib.GameProfile
import me.redth.statsoverlay.data.stats.StatsTypes
import me.redth.statsoverlay.util.HypixelAPI

open class FetchingStatsMap(
    override val profile: GameProfile
) : StatsMap {
    init {
        runAsync { fetchAPI() }
    }

    private fun fetchAPI() {
        val response = HypixelAPI.getPlayerResponse(profile.id) ?: return
        val map = StatsTypes.ALL.associateWith { type ->
            type.format(response)
        }
        val statsMap = LoadedStatsMap(profile, response, map)
        StatsMaps.load(profile, statsMap)
    }
}