package me.redth.statsoverlay.stat.bedwars;

public enum BedwarsPrestige {
    STONE("§7[x✫]", false),
    IRON("§f[x✫]", false),
    GOLD("§6[x✫]", false),
    DIAMOND("§b[x✫]", false),
    EMERALD("§2[x✫]", false),
    SAPPHIRE("§3[x✫]", false),
    RUBY("§4[x✫]", false),
    CRYSTAL("§d[x✫]", false),
    OPAL("§9[x✫]", false),
    AMETHYST("§5[x✫]", false),
    RAINBOW("§c[§6w§ex§ay§bz§d✫§5]", true),
    IRON_PRIME("§7[§fx§7✪§7]", false),
    GOLD_PRIME("§7[§ex§6✪§7]", false),
    DIAMOND_PRIME("§7[§bx§3✪§7]", false),
    EMERALD_PRIME("§7[§ax§2✪§7]", false),
    SAPPHIRE_PRIME("§7[§3x§9✪§7]", false),
    RUBY_PRIME("§7[§cx§4✪§7]", false),
    CRYSTAL_PRIME("§7[§dx§5✪§7]", false),
    OPAL_PRIME("§7[§9x§1✪§7]", false),
    AMETHYST_PRIME("§7[§5x§8✪§7]", false),
    MIRROR("§8[§7w§fxy§7z§8✪]", true),
    LIGHT("§f[w§exy§6z§l⚝§6]", true),
    DAWN("§6[w§fxy§bz§3§l⚝§3]", true),
    DUSK("§5[w§dxy§6z§e§l⚝§e]", true),
    AIR("§b[w§fxy§7z§l⚝§8]", true),
    WIND("§f[w§axy§2z§l⚝§2]", true),
    NEBULA("§4[w§cxy§dz§l⚝§d]", true),
    THUNDER("§e[w§fxy§8z§l⚝§8]", true),
    EARTH("§a[w§2xy§6z§l⚝§e]", true),
    WATER("§b[w§3xy§9z§l⚝§1]", true),
    FIRE("§e[w§6xy§cz§l⚝§4]", true);

    private final String format;
    private final boolean splitNumbers;

    BedwarsPrestige(String format, boolean splitNumbers) {
        this.format = format;
        this.splitNumbers = splitNumbers;
    }

    public static String format(int stars) {
        BedwarsPrestige prestige = getPrestige(stars);
        String starsString = String.valueOf(stars);
        if (prestige.splitNumbers)
            return prestige.format.replace('w', starsString.charAt(0)).replace('x', starsString.charAt(1)).replace('y', starsString.charAt(2)).replace('z', starsString.charAt(3));
        else
            return prestige.format.replace("x", starsString);

    }

    public static BedwarsPrestige getPrestige(int stars) {
        if (stars < 0 || stars / 100 >= values().length) return STONE;
        return values()[stars / 100];
    }
}
