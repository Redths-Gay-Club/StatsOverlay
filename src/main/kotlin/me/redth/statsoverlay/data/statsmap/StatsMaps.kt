package me.redth.statsoverlay.data.statsmap

import com.mojang.authlib.GameProfile
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.data.stats.StatsTypes
import java.util.UUID

object StatsMaps {
    private val statsMapMap = HashMap<UUID, StatsMap>()
    var sortedStatsMap: List<StatsMap> = emptyList()
        private set

    fun clear() {
        synchronized(this) {
            statsMapMap.clear()
            sortedStatsMap = emptyList()
        }
    }

    fun add(profile: GameProfile) {
        if (profile.id.version() == 2) return // is NPC
        synchronized(this) {
            statsMapMap[profile.id] = FetchingStatsMap(profile)
            updateSortedList()
        }
    }

    fun load(profile: GameProfile, statsMap: StatsMap) {
        synchronized(this) {
            statsMapMap.replace(profile.id, statsMap)
            updateSortedList()
        }
    }

    fun remove(profile: GameProfile) {
        synchronized(this) {
            statsMapMap.remove(profile.id)
            updateSortedList()
        }
    }

    private fun updateSortedList() {
        sortedStatsMap = statsMapMap.values.sortedWith(sorter)
    }

    private val sorter get() = StatsTypes.ALL[ModConfig.sorting]
}