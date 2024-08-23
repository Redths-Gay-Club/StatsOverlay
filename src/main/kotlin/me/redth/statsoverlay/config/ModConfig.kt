package me.redth.statsoverlay.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.redth.statsoverlay.StatsOverlay
import me.redth.statsoverlay.data.Column
import me.redth.statsoverlay.util.HypixelAPI
import org.lwjgl.input.Keyboard

object ModConfig : Config(Mod(StatsOverlay.NAME, ModType.HYPIXEL), "${StatsOverlay.MODID}.json") {
    @Info(text = "Don't forget to set your API key", type = InfoType.INFO)
    var ignored = false

    @KeyBind(name = "Overlay Keybind")
    var keyBind = OneKeyBind(Keyboard.KEY_TAB)

    @DualOption(name = "Keybind Type", left = "Hold", right = "Toggle")
    var toggleKeyBind = false

    @DualOption(name = "Show in", left = "Only in Bedwars", right = "Any Server")
    var showAnywhere = false

    @Dropdown(name = "Sort by", options = ["Stars", "Wins", "WLR", "Final Kills", "FKDR", "Kills", "KDR", "Beds Broken", "BBLR", "Winstreak"])
    var sorting = 0

    @Checkbox(name = "Sort by teams after game started")
    var sortByTeams = true

    @Checkbox(name = "Wins", subcategory = "Bedwars")
    var bwWins = true

    @Checkbox(name = "WLR", subcategory = "Bedwars")
    var bwWlr = true

    @Checkbox(name = "Final Kills", subcategory = "Bedwars")
    var bwFinalKills = true

    @Checkbox(name = "FKDR", subcategory = "Bedwars")
    var bwFkdr = true

    @Checkbox(name = "Kills", subcategory = "Bedwars")
    var bwKills = true

    @Checkbox(name = "KDR", subcategory = "Bedwars")
    var bwKdr = true

    @Checkbox(name = "Beds Broken", subcategory = "Bedwars")
    var bwBedsBroken = true

    @Checkbox(name = "BBLR", subcategory = "Bedwars")
    var bwBblr = true

    @Checkbox(name = "Winstreak", subcategory = "Bedwars")
    var bwWinstreak = true

    @HUD(name = "Overlay", subcategory = "HUD")
    var hud = OverlayHUD()

    @DualOption(name = "API", category = "API", left = "Ursa Minor", right = "Hypixel API", size = 2)
    var useHypixelAPI = true

    @Text(name = "API Key", category = "API", secure = true)
    var hypixelApiKey = ""

    @Button(name = "Clear cache", category = "Debug", text = "Clear")
    fun clearCache() {
        HypixelAPI.clearCache()
    }

    fun shouldShow(column: Column): Boolean = when (column) {
        Column.STAR, Column.NAME -> true
        Column.WINS -> bwWins
        Column.WLR -> bwWlr
        Column.FINAL_KILLS -> bwFinalKills
        Column.FKDR -> bwFkdr
        Column.KILLS -> bwKills
        Column.KDR -> bwKdr
        Column.BEDS_BROKEN -> bwBedsBroken
        Column.BBLR -> bwBblr
        Column.WINSTREAK -> bwWinstreak
    }

    fun getComparing(): Column = when (sorting) {
        0 -> Column.STAR
        1 -> Column.WINS
        2 -> Column.WLR
        3 -> Column.FINAL_KILLS
        4 -> Column.FKDR
        5 -> Column.KILLS
        6 -> Column.KDR
        7 -> Column.BEDS_BROKEN
        8 -> Column.BBLR
        9 -> Column.WINSTREAK
        else -> Column.STAR
    }

    init {
        initialize()
    }
}