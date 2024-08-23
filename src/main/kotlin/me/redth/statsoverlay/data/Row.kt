package me.redth.statsoverlay.data

import cc.polyfrost.oneconfig.utils.dsl.mc
import me.redth.statsoverlay.bedwars.BedwarsPrestige
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.util.PlayerObject
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.MathHelper
import java.util.concurrent.Future

sealed interface Row {
    val name: String
    fun getText(column: Column): ColoredText

    fun getTeamNameForSorting(): String =
        mc.theWorld?.scoreboard?.getPlayersTeam(name)?.registeredName?.takeIf { it.isColorName() } ?: ""

    fun getFormattedName(): ColoredText {
        val world = mc.theWorld ?: return ColoredText(name, alignment = Alignment.LEFT)
        val team = world.scoreboard.getPlayersTeam(name)
        return ColoredText(ScorePlayerTeam.formatPlayerName(team, name), alignment = Alignment.LEFT)
    }
}

private val colorName = listOf(
    "Aqua",
    "Blue",
    "Gray",
    "Green",
    "Pink",
    "Red",
    "White",
    "Yellow",
) // hypixel team name is something like Red12 Blue31

private fun String.isColorName() = filter { it.isLetter() } in colorName

class Loading(
    override val name: String,
    private val task: Future<*>,
) : Row {
    fun cancel() {
        task.cancel(false)
    }

    override fun getText(column: Column) = when (column) {
        Column.NAME -> getFormattedName()
        Column.STAR -> ColoredText.NICKED
        else -> ColoredText.UNKNOWN
    }
}

class Nicked(
    override val name: String,
) : Row {
    override fun getText(column: Column) = when (column) {
        Column.NAME -> getFormattedName()
        Column.STAR -> ColoredText.NICKED
        else -> ColoredText.UNKNOWN
    }
}

class Loaded(
    override val name: String,
    private val stats: PlayerObject,
) : Row, Comparable<Loaded> {
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

    override fun getText(column: Column) = when (column) {
        Column.NAME -> getFormattedName()
        else -> map[column] ?: ColoredText.UNKNOWN
    }

    override fun compareTo(other: Loaded) = getComparator().compare(this, other)

    private fun getComparator(): Comparator<Loaded> = when (ModConfig.getComparing()) {
        Column.STAR -> compareByDescending { it.stats.stars }
        Column.FINAL_KILLS -> compareByDescending { it.stats.finalKills }
        Column.FKDR -> compareByDescending { it.stats.fkdr }
        Column.WINS -> compareByDescending { it.stats.wins }
        Column.WLR -> compareByDescending { it.stats.wlr }
        Column.KILLS -> compareByDescending { it.stats.kills }
        Column.KDR -> compareByDescending { it.stats.kdr }
        Column.BEDS_BROKEN -> compareByDescending { it.stats.bedsBroken }
        Column.BBLR -> compareByDescending { it.stats.bblr }
        Column.WINSTREAK -> compareByDescending { it.stats.winstreak }
        else -> compareByDescending { it.stats.stars }
    }
}

private fun format(value: Int, godLevel: Int): ColoredText {
    val degree = (godLevel - value).coerceAtLeast(0) / godLevel.toFloat() * 0.5f
    return ColoredText(
        "%,d".format(value),
        MathHelper.hsvToRGB(degree, 0.7f, 1f),
        Alignment.RIGHT
    )
}

private fun format(value: Float, godLevel: Float): ColoredText {
    val degree = (godLevel - value).coerceAtLeast(0f) / godLevel * 0.5f
    return ColoredText(
        "%.2f".format(value),
        MathHelper.hsvToRGB(degree, 0.7f, 1f),
        Alignment.RIGHT
    )
}

private fun Row.order() = when (this) {
    is Nicked -> 0
    is Loaded -> 1
    is Loading -> 2
}

object RowComparator : Comparator<Row> {
    override fun compare(o1: Row, o2: Row): Int = when {
        o1 is Loaded && o2 is Loaded -> o1.compareTo(o2)
        else -> o1.order() - o2.order()
    }
}

