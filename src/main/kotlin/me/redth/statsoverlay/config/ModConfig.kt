package me.redth.statsoverlay.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.redth.statsoverlay.StatsOverlay
import org.lwjgl.input.Keyboard

object ModConfig : Config(Mod(StatsOverlay.NAME, ModType.HYPIXEL), "${StatsOverlay.MODID}.json") {
    init {
        initialize()
    }

    @KeyBind(name = "Overlay Key Bind")
    var keyBind = OneKeyBind(Keyboard.KEY_TAB)

    @DualOption(name = "Key Bind Mode", left = "Hold", right = "Toggle")
    var toggleKeyBind = false

    @DualOption(name = "Show in", left = "Only in Bedwars", right = "Any Server")
    var showAnyWhere = false

    @Dropdown(name = "Sorting", options = ["Stars", "Final Kills", "FKDR", "Wins", "WLR", "Beds Broken", "BBLR", "Winstreak"])
    var sorting = 0

    @Text(name = "Self Name")
    var selfName = "§cYou"

    @HUD(name = "Overlay")
    var hud = OverlayHUD()

    @Checkbox(name = "Final Kills", category = "Bedwars")
    var bwFinalKills = true

    @Checkbox(name = "FKDR", category = "Bedwars")
    var bwFkdr = true

    @Checkbox(name = "Wins", category = "Bedwars")
    var bwWins = true

    @Checkbox(name = "WLR", category = "Bedwars")
    var bwWlr = true

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
}