package me.redth.statsoverlay.util

import com.google.gson.annotations.SerializedName

data class PlayerResponse(
    val player: PlayerObject?,
)

data class PlayerObject(
    private val achievements: Achievements,
    private val stats: Stats,
) {
    val stars get() = achievements.bedwarsLevel
    val finalKills get() = stats.bedwars.finalKills
    val finalDeaths get() = stats.bedwars.finalDeaths
    val kills get() = stats.bedwars.kills
    val deaths get() = stats.bedwars.deaths
    val wins get() = stats.bedwars.wins
    val losses get() = stats.bedwars.losses
    val bedsBroken get() = stats.bedwars.bedsBroken
    val bedsLost get() = stats.bedwars.bedsLost
    val winstreak get() = stats.bedwars.winstreak
    val fkdr get() = ratio(finalKills, finalDeaths)
    val kdr get() = ratio(kills, deaths)
    val wlr get() = ratio(wins, losses)
    val bblr get() = ratio(bedsBroken, bedsLost)
}

data class Achievements(
    @SerializedName("bedwars_level") val bedwarsLevel: Int,
)

data class Stats(
    @SerializedName("Bedwars") val bedwars: Bedwars,
)

data class Bedwars(
    @SerializedName("final_kills_bedwars") val finalKills: Int,
    @SerializedName("final_deaths_bedwars") val finalDeaths: Int,
    @SerializedName("kills_bedwars") val kills: Int,
    @SerializedName("deaths_bedwars") val deaths: Int,
    @SerializedName("wins_bedwars") val wins: Int,
    @SerializedName("losses_bedwars") val losses: Int,
    @SerializedName("beds_broken_bedwars") val bedsBroken: Int,
    @SerializedName("beds_lost_bedwars") val bedsLost: Int,
    @SerializedName("winstreak") val winstreak: Int,
) {
}

private fun ratio(a: Int, b: Int): Float = a.toFloat() / b.coerceAtLeast(1).toFloat()
