package me.redth.statsoverlay.stat;

import me.redth.statsoverlay.config.OverlayConfig;
import me.redth.statsoverlay.stat.bedwars.BedwarsPrestige;
import net.minecraft.util.MathHelper;

import java.util.Comparator;

public enum StatType implements Comparator<PlayerData> {
    NAME("Name") {
        @Override
        public boolean shouldShow() {
            return true;
        }
    }, STARS("Stars") {
        @Override
        public String format(Number n) {
            return BedwarsPrestige.format(n.intValue());
        }

        @Override
        public int getColor(Number n) {
            return 0xFFFFFF;
        }

        @Override
        public boolean shouldShow() {
            return !OverlayConfig.prefixStars;
        }
    }, FINAL_KILLS("Finals", 20000) {
        @Override
        public boolean shouldShow() {
            return OverlayConfig.finalKills;
        }
    }, FKDR("FKDR", 10F) {
        @Override
        public boolean shouldShow() {
            return OverlayConfig.fkdr;
        }
    }, WINS("Wins", 10000) {
        @Override
        public boolean shouldShow() {
            return OverlayConfig.wins;
        }
    }, WLR("WLR", 5F) {
        @Override
        public boolean shouldShow() {
            return OverlayConfig.wlr;
        }
    }, BEDS_BROKEN("Beds", 10000) {
        @Override
        public boolean shouldShow() {
            return OverlayConfig.bedsBroken;
        }
    }, BBLR("BBLR", 5F) {
        @Override
        public boolean shouldShow() {
            return OverlayConfig.bblr;
        }
    };

    public static final StatType[] SORTABLE = new StatType[] {STARS, FINAL_KILLS, FKDR, WINS, WLR, BEDS_BROKEN, BBLR};

    public final String name;
    public final boolean isInt;
    public final float colorCap;
    public final boolean rightAlign;

    StatType(String name, Number colorCap) {
        this.name = name;
        this.isInt = colorCap instanceof Integer;
        this.colorCap = colorCap.floatValue();
        this.rightAlign = true;
    }

    StatType(String name) {
        this.name = name;
        this.isInt = true;
        this.colorCap = 0;
        this.rightAlign = false;
    }

    public String format(Number n) {
        return isInt ? String.format("%,d", n.intValue()) : String.format("%.2f", n.floatValue());
    }

    public int getColor(Number n) {
        float degree = (colorCap - Math.min(colorCap, n.floatValue())) / colorCap / 2F;
        return MathHelper.hsvToRGB(degree, 0.7f, 1f);
    }

    public abstract boolean shouldShow();

    @Override
    public int compare(PlayerData data1, PlayerData data2) {
        int compareFetching = Boolean.compare(data1.isStatsNull(), data2.isStatsNull());
        if (compareFetching != 0) return compareFetching;
        int compareNicked = Boolean.compare(data2.isNicked(), data1.isNicked());
        if (compareNicked != 0) return compareNicked;
        return Float.compare(data2.getStatRaw(this).floatValue(), data1.getStatRaw(this).floatValue());
    }
}
