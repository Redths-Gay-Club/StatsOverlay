package me.redth.statsoverlay.data.stats

import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.PlayerResponse
import me.redth.statsoverlay.data.statsmap.StatsMap

interface StatsType : Comparator<StatsMap> {
    val title: String

    fun format(stats: PlayerResponse?): ColoredText

    fun shouldShow(): Boolean

    fun compareStats(o1: PlayerResponse, o2: PlayerResponse): Int

    override fun compare(o1: StatsMap, o2: StatsMap): Int {
        val response1 = o1.response
        val response2 = o2.response

        response1 ?: response2 ?: return 0 // if both havent loaded
        response1 ?: return 1 // if o1 hasnt loaded put it lower
        response2 ?: return -1 // if o2 hasnt loaded put it higher

        val compareNicked = response1.success.compareTo(response2.success)
        if (compareNicked != 0) return compareNicked
        return -compareStats(response1, response2)
    }
}