package me.redth.statsoverlay.data

import com.google.gson.annotations.SerializedName

data class PlayerResponse(
    val success: Boolean,
    val player: PlayerObject?,
)

data class PlayerObject(
    val achievements: Achievements?,
    val stats: Stats?,
)

data class Achievements(
    @SerializedName("bedwars_level") val bedwarsLevel: Int,
)

data class Stats(
    @SerializedName("Bedwars") val bedwars: Bedwars,
)

data class Bedwars(
    @SerializedName("final_kills_bedwars") val finalKills: Int,
    @SerializedName("final_deaths_bedwars") val finalDeaths: Int,
    @SerializedName("wins_bedwars") val wins: Int,
    @SerializedName("losses_bedwars") val losses: Int,
    @SerializedName("beds_broken_bedwars") val bedsBroken: Int,
    @SerializedName("beds_lost_bedwars") val bedsLost: Int,
    @SerializedName("winstreak") val winstreak: Int,
) {
    val fkdr get() = finalKills.toFloat() / finalDeaths.toFloat().coerceAtLeast(1f)
    val wlr get() = wins.toFloat() / losses.toFloat().coerceAtLeast(1f)
    val bblr get() = bedsBroken.toFloat() / bedsLost.toFloat().coerceAtLeast(1f)
}
