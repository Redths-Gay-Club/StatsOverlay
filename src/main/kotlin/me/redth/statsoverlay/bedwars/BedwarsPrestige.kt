package me.redth.statsoverlay.bedwars

import me.redth.statsoverlay.data.ColoredText

enum class BedwarsPrestige(
    private val format: String,
) {
    STONE("§7[_✫]"),
    IRON("§f[_✫]"),
    GOLD("§6[_✫]"),
    DIAMOND("§b[_✫]"),
    EMERALD("§2[_✫]"),
    SAPPHIRE("§3[_✫]"),
    RUBY("§4[_✫]"),
    CRYSTAL("§d[_✫]"),
    OPAL("§9[_✫]"),
    AMETHYST("§5[_✫]"),
    RAINBOW("§c[§6_§e_§a_§b_§d✫§5]"),
    IRON_PRIME("§7[§f_§7✪§7]"),
    GOLD_PRIME("§7[§e_§6✪§7]"),
    DIAMOND_PRIME("§7[§b_§3✪§7]"),
    EMERALD_PRIME("§7[§a_§2✪§7]"),
    SAPPHIRE_PRIME("§7[§3_§9✪§7]"),
    RUBY_PRIME("§7[§c_§4✪§7]"),
    CRYSTAL_PRIME("§7[§d_§5✪§7]"),
    OPAL_PRIME("§7[§9_§1✪§7]"),
    AMETHYST_PRIME("§7[§5_§8✪§7]"),
    MIRROR("§8[§7_§f__§7_§8✪]"),
    LIGHT("§f[_§e__§6_§l⚝§6]"),
    DAWN("§6[_§f__§b_§3§l⚝§3]"),
    DUSK("§5[_§d__§6_§e§l⚝§e]"),
    AIR("§b[_§f__§7_§l⚝§8]"),
    WIND("§f[_§a__§2_§l⚝§2]"),
    NEBULA("§4[_§c__§d_§l⚝§d]"),
    THUNDER("§e[_§f__§8_§l⚝§8]"),
    EARTH("§a[_§2__§6_§l⚝§e]"),
    WATER("§b[_§3__§9_§l⚝§1]"),
    FIRE("§e[_§6__§c_§l⚝§4]");

    private val shouldSplitDigits: Boolean = format.count { it == '_' } > 1

    companion object {
        fun getPrefix(stars: Int) = ColoredText(format(stars) + " §r")

        fun format(stars: Int): String {
            val prestige = getPrestige(stars)
            val starsString = stars.toString()

            if (!prestige.shouldSplitDigits) return prestige.format.replaceFirst("_", starsString)

            return prestige.format
                .replaceFirst('_', starsString[0])
                .replaceFirst('_', starsString[1])
                .replaceFirst('_', starsString[2])
                .replaceFirst('_', starsString[3])
        }

        private fun getPrestige(stars: Int) = entries.getOrNull(stars / 100) ?: STONE
    }
}
