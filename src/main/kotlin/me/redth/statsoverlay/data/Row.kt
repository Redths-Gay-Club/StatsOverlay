package me.redth.statsoverlay.data

import me.redth.statsoverlay.bedwars.BedwarsPrestige
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.util.PlayerObject
import net.minecraft.util.MathHelper

sealed interface Row : Comparable<Row> {
    fun getText(column: Column): ColoredText

    data object Loading : Row {
        override fun getText(column: Column) = ColoredText.UNKNOWN

        override fun compareTo(other: Row) = when (other) {
            is Loading -> 0
            else -> 1
        }
    }

    data object Nicked : Row {
        override fun getText(column: Column) = when (column) {
            Column.STAR -> ColoredText.NICKED
            else -> ColoredText.UNKNOWN
        }

        override fun compareTo(other: Row) = when (other) {
            is Nicked -> 0
            else -> -1
        }
    }

    class Loaded(private val stats: PlayerObject) : Row {
        private val map: Map<Column, ColoredText> = mapOf(
            Column.STAR to BedwarsPrestige.getPrefix(stats.stars),
            Column.WINS to format(stats.wins, 10000),
            Column.WLR to format(stats.wlr, 5f),
            Column.FINAL_KILLS to format(stats.finalKills, 20000),
            Column.FKDR to format(stats.fkdr, 10f),
            Column.KILLS to format(stats.kills, 40000),
            Column.KDR to format(stats.kdr, 1f),
            Column.BEDS_BROKEN to format(stats.bedsBroken, 10000),
            Column.BBLR to format(stats.bblr, 5f),
            Column.WINSTREAK to format(stats.winstreak, 30),
        )

        override fun getText(column: Column) = map[column] ?: ColoredText.UNKNOWN

        override fun compareTo(other: Row) = when (other) {
            Loading -> -1
            Nicked -> 1
            is Loaded -> getComparator().compare(this, other)
        }

        private fun getComparator(): Comparator<Loaded> = when (ModConfig.getComparing()) {
            Column.STAR -> compareBy { it.stats.stars }
            Column.FINAL_KILLS -> compareBy { it.stats.finalKills }
            Column.FKDR -> compareBy { it.stats.fkdr }
            Column.WINS -> compareBy { it.stats.wins }
            Column.WLR -> compareBy { it.stats.wlr }
            Column.KILLS -> compareBy { it.stats.kills }
            Column.KDR -> compareBy { it.stats.kdr }
            Column.BEDS_BROKEN -> compareBy { it.stats.bedsBroken }
            Column.BBLR -> compareBy { it.stats.bblr }
            Column.WINSTREAK -> compareBy { it.stats.winstreak }
            else -> compareBy { it.stats.stars }
        }
    }
}


private fun format(value: Int, godLevel: Int): ColoredText {
    val degree = (godLevel - value).coerceAtLeast(0) / godLevel.toFloat() * 0.5f
    return ColoredText(
        "%,d".format(value),
        MathHelper.hsvToRGB(degree, 0.7f, 1f)
    )
}

private fun format(value: Float, godLevel: Float): ColoredText {
    val degree = (godLevel - value).coerceAtLeast(0f) / godLevel * 0.5f
    return ColoredText(
        "%.2f".format(value),
        MathHelper.hsvToRGB(degree, 0.7f, 1f)
    )
}
