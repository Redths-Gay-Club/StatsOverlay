package me.redth.statsoverlay.data

import com.mojang.authlib.GameProfile
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.util.HypixelAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.math.max

object FetchQueue {
    private val executor = Executors.newFixedThreadPool(3)
    private val map = ConcurrentHashMap<UUID, Row>()

    fun add(profile: GameProfile) {
        if (map[profile.id] != null) return
        map[profile.id] = Loading(profile.name, executor.submit { fetch(profile.id) })
    }

    fun remove(profile: GameProfile) {
        val removed = map.remove(profile.id)
        if (removed is Loading) removed.cancel()
    }

    fun clear() {
        for ((_, row) in map) {
            if (row is Loading) row.cancel()
        }
        map.clear()
    }

    private fun fetch(uuid: UUID) {
        val response = HypixelAPI.getStatsOrCached(uuid) ?: return
        val row = map[uuid]
        if (row !is Loading) return // removed or already loaded
        map[uuid] = when (val stats = response.player) {
            null -> Nicked(row.name)
            else -> Loaded(row.name, stats)
        }
    }

    fun makeGrid(): CompiledGrid {
        val shown = Column.entries.filter { ModConfig.shouldShow(it) }
        val widthArray = IntArray(shown.size)
        val result = buildList {
            add(shown.map { column -> ColoredText(column.label) })
            map.values
                .sortedWith(getComparator())
                .mapTo(this) { row ->
                    shown.map { column ->
                        if (column == Column.NAME) row.getFormattedName()
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

private fun getComparator(): Comparator<Row> = when {
    ModConfig.sortByTeams -> compareBy<Row> { row -> row.getTeamName() }.thenComparing(RowComparator)
    else -> RowComparator
}

