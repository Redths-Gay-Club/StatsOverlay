package me.redth.statsoverlay.bedwars

import me.redth.statsoverlay.data.ColoredText

enum class BedwarsPrestige(private val format: String, private val splitNumbers: Boolean) {
    STONE("§7[_✫]", false),
    IRON("§f[_✫]", false),
    GOLD("§6[_✫]", false),
    DIAMOND("§b[_✫]", false),
    EMERALD("§2[_✫]", false),
    SAPPHIRE("§3[_✫]", false),
    RUBY("§4[_✫]", false),
    CRYSTAL("§d[_✫]", false),
    OPAL("§9[_✫]", false),
    AMETHYST("§5[_✫]", false),
    RAINBOW("§c[§6w§e_§a_§b_§d✫§5]", true),
    IRON_PRIME("§7[§f_§7✪§7]", false),
    GOLD_PRIME("§7[§e_§6✪§7]", false),
    DIAMOND_PRIME("§7[§b_§3✪§7]", false),
    EMERALD_PRIME("§7[§a_§2✪§7]", false),
    SAPPHIRE_PRIME("§7[§3_§9✪§7]", false),
    RUBY_PRIME("§7[§c_§4✪§7]", false),
    CRYSTAL_PRIME("§7[§d_§5✪§7]", false),
    OPAL_PRIME("§7[§9_§1✪§7]", false),
    AMETHYST_PRIME("§7[§5_§8✪§7]", false),
    MIRROR("§8[§7_§f__§7_§8✪]", true),
    LIGHT("§f[_§e__§6_§l⚝§6]", true),
    DAWN("§6[_§f__§b_§3§l⚝§3]", true),
    DUSK("§5[_§d__§6_§e§l⚝§e]", true),
    AIR("§b[_§f__§7_§l⚝§8]", true),
    WIND("§f[_§a__§2_§l⚝§2]", true),
    NEBULA("§4[_§c__§d_§l⚝§d]", true),
    THUNDER("§e[_§f__§8_§l⚝§8]", true),
    EARTH("§a[_§2__§6_§l⚝§e]", true),
    WATER("§b[_§3__§9_§l⚝§1]", true),
    FIRE("§e[_§6__§c_§l⚝§4]", true);

    companion object {
        operator fun get(stars: Int) = ColoredText(format(stars) + " §r")

        private fun format(stars: Int): String {
            val prestige = getPrestige(stars)
            val starsString = stars.toString()

            if (!prestige.splitNumbers) return prestige.format.replaceFirst("_", starsString)

            return prestige.format
                .replaceFirst('_', starsString[0])
                .replaceFirst('_', starsString[1])
                .replaceFirst('_', starsString[2])
                .replaceFirst('_', starsString[3])
        }

        private fun getPrestige(stars: Int) = values().getOrNull(stars / 100) ?: STONE
    }
}
