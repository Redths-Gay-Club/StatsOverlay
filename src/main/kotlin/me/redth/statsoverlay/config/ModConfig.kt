package me.redth.statsoverlay.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.redth.statsoverlay.StatsOverlay
import me.redth.statsoverlay.data.Column
import org.lwjgl.input.Keyboard

object ModConfig : Config(Mod(StatsOverlay.NAME, ModType.HYPIXEL), "${StatsOverlay.MODID}.json") {
    init {
        initialize()
    }

    @KeyBind(name = "Overlay Keybind")
    var keyBind = OneKeyBind(Keyboard.KEY_TAB)

    @DualOption(name = "Keybind Type", left = "Hold", right = "Toggle")
    var toggleKeyBind = false

    @DualOption(name = "Show in", left = "Only in Bedwars", right = "Any Server")
    var showAnywhere = false

    @Dropdown(name = "Sort by", options = ["Stars", "Teams", "Final Kills", "FKDR", "Wins", "WLR", "Kills", "KDR", "Beds Broken", "BBLR", "Winstreak"])
    var sorting = 0

    @HUD(name = "Overlay")
    var hud = OverlayHUD()

    @Checkbox(name = "Wins", category = "Bedwars")
    var bwWins = true

    @Checkbox(name = "WLR", category = "Bedwars")
    var bwWlr = true

    @Checkbox(name = "Final Kills", category = "Bedwars")
    var bwFinalKills = true

    @Checkbox(name = "FKDR", category = "Bedwars")
    var bwFkdr = true

    @Checkbox(name = "Kills", category = "Bedwars")
    var bwKills = true

    @Checkbox(name = "KDR", category = "Bedwars")
    var bwKdr = true

    @Checkbox(name = "Beds Broken", category = "Bedwars")
    var bwBedsBroken = true

    @Checkbox(name = "BBLR", category = "Bedwars")
    var bwBblr = true

    @Checkbox(name = "Winstreak", category = "Bedwars")
    var bwWinstreak = true

    @DualOption(name = "API", category = "API", left = "Ursa Minor", right = "Hypixel API", size = 2)
    var useHypixelAPI = true

    @Text(name = "API Key", category = "API", secure = true)
    var hypixelApiKey = ""

    @Transient
    private var keyBindToggled = false

    @Transient
    private var lastKeyBindActive = false

    val isKeyPressed: Boolean
        get() {
            if (lastKeyBindActive != keyBind.isActive) {
                lastKeyBindActive = keyBind.isActive
                if (lastKeyBindActive) keyBindToggled = !keyBindToggled && toggleKeyBind
            }

            if (toggleKeyBind) return keyBindToggled
            return lastKeyBindActive
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
        1 -> Column.NAME
        2 -> Column.WINS
        3 -> Column.WLR
        4 -> Column.FINAL_KILLS
        5 -> Column.FKDR
        6 -> Column.KILLS
        7 -> Column.KDR
        8 -> Column.BEDS_BROKEN
        9 -> Column.BBLR
        10 -> Column.WINSTREAK
        else -> Column.STAR
    }
}