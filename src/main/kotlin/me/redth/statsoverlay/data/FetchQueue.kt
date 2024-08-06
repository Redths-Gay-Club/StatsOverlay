package me.redth.statsoverlay.data

import cc.polyfrost.oneconfig.utils.dsl.mc
import com.mojang.authlib.GameProfile
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.util.HypixelAPI
import net.minecraft.scoreboard.ScorePlayerTeam
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.math.max

object FetchQueue {
    private val executor = Executors.newFixedThreadPool(3)
    private val map = ConcurrentHashMap<GameProfile, Row>()

    fun add(profile: GameProfile) {
        map[profile] = Row.Loading
        executor.submit { fetch(profile) }
    }

    fun remove(profile: GameProfile) {
        map.remove(profile)
    }

    fun clear() {
        map.clear()
    }

    private fun fetch(profile: GameProfile) {
        val response = HypixelAPI.getPlayerResponse(profile.id) ?: return
        map[profile] = when (val stats = response.player) {
            null -> Row.Nicked
            else -> Row.Loaded(stats)
        }
    }

    fun makeGrid(): CompiledGrid {
        val shown = Column.entries.filter { ModConfig.shouldShow(it) }
        val widthArray = IntArray(shown.size)
        val result = buildList {
            add(shown.map { column -> ColoredText(column.name) })
            map.entries
                .sortedBy { (_, row) -> row }
                .mapTo(this) { (profile, row) ->
                    shown.map { column ->
                        if (column == Column.NAME) profile.getFormattedName()
                        else row.getText(column)
                    }
                }
        }
        for (row in result) {
            for ((index, text) in row.withIndex()) {
                widthArray[index] = max(widthArray[index], text.width + 4)
            }
        }
        return CompiledGrid(
            result,
            widthArray.toList(),
            widthArray.sum(),
            result.size * 10
        )
    }
}

private fun GameProfile.getFormattedName(): ColoredText {
    val world = mc.theWorld ?: return ColoredText(name)
    val team = world.scoreboard.getPlayersTeam(name)
    return ColoredText(ScorePlayerTeam.formatPlayerName(team, name))
}