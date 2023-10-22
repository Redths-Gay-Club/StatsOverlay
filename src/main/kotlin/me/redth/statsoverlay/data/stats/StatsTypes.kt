package me.redth.statsoverlay.data.stats

import me.redth.statsoverlay.bedwars.BedwarsPrestige
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.PlayerResponse

object StatsTypes {
    val BW_STARS_PREFIX = object : IntStats("Name", 0) {
        override fun shouldShow() = true
        override fun getStats(stats: PlayerResponse?) = stats?.player?.achievements?.bedwarsLevel
        override fun format(stats: PlayerResponse?): ColoredText {
            if (stats?.success == false) return ColoredText.NICKED
            return BedwarsPrestige[getStats(stats) ?: 0]
        }
    }
    val BW_FINAL_KILLS = object : IntStats("Finals", 20000) {
        override fun shouldShow() = ModConfig.bwFinalKills
        override fun getStats(stats: PlayerResponse?) = stats?.player?.stats?.bedwars?.finalKills
    }
    val BW_FKDR = object : FloatStats("FKDR", 10f) {
        override fun shouldShow() = ModConfig.bwFkdr
        override fun getStats(stats: PlayerResponse?) = stats?.player?.stats?.bedwars?.fkdr
    }
    val BW_WINS = object : IntStats("Wins", 10000) {
        override fun shouldShow() = ModConfig.bwWins
        override fun getStats(stats: PlayerResponse?) = stats?.player?.stats?.bedwars?.wins
    }
    val BW_WLR = object : FloatStats("WLR", 5f) {
        override fun shouldShow() = ModConfig.bwWlr
        override fun getStats(stats: PlayerResponse?) = stats?.player?.stats?.bedwars?.wlr
    }
    val BW_BEDS_BROKEN = object : IntStats("Beds", 10000) {
        override fun shouldShow() = ModConfig.bwBedsBroken
        override fun getStats(stats: PlayerResponse?) = stats?.player?.stats?.bedwars?.bedsBroken
    }
    val BW_BBLR = object : FloatStats("BBLR", 5f) {
        override fun shouldShow() = ModConfig.bwBblr
        override fun getStats(stats: PlayerResponse?) = stats?.player?.stats?.bedwars?.bblr
    }
    val BW_WINSTREAK = object : IntStats("Winstreak", 30) {
        override fun shouldShow() = ModConfig.bwWinstreak
        override fun getStats(stats: PlayerResponse?) = stats?.player?.stats?.bedwars?.winstreak
    }

    val ALL = listOf(BW_STARS_PREFIX, BW_FINAL_KILLS, BW_FKDR, BW_WINS, BW_WLR, BW_BEDS_BROKEN, BW_BBLR, BW_WINSTREAK)
}