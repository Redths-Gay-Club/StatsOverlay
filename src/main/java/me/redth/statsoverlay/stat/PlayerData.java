package me.redth.statsoverlay.stat;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.wrappers.UPlayer;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import me.redth.statsoverlay.config.OverlayConfig;
import me.redth.statsoverlay.util.JsonNullSafe;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private Map<StatType, Data> stats;
    private boolean nicked;
    public final boolean isSelf;
    public final String name;

    public PlayerData(GameProfile profile) {
        this.isSelf = profile.getId().equals(UPlayer.getUUID());
        this.name = profile.getName();

        new Thread(() -> {
            stats = DataList.CACHE.get(profile.getId(), PlayerData::getStatMap);
            nicked = stats != null && stats.isEmpty();
            DataList.altered = true;
        }).start();
    }

    public static Map<StatType, Data> getStatMap(UUID uuid) {
        String url = "https://api.hypixel.net/player?key=" + OverlayConfig.apiKey + "&uuid=" + uuid.toString();

        JsonObject response = JsonNullSafe.getAsObject(NetworkUtils.getJsonElement(url));
        if (response == null) return null;
        if (!JsonNullSafe.getBoolean(response, "success")) return null;

        Map<StatType, Data> statMap = new HashMap<>();

        JsonObject player = JsonNullSafe.getObject(response, "player");
        if (player == null) return statMap;

        put(statMap, StatType.STARS, JsonNullSafe.getInt(JsonNullSafe.getObject(player, "achievements"), "bedwars_level"));

        JsonObject bedwars = JsonNullSafe.getObject(JsonNullSafe.getObject(player, "stats"), "Bedwars");

        int finalKills = JsonNullSafe.getInt(bedwars, "final_kills_bedwars");
        int finalDeaths = JsonNullSafe.getInt(bedwars, "final_deaths_bedwars");
        float fkdr = finalDeaths == 0 ? finalKills : ((float) finalKills / finalDeaths);

        put(statMap, StatType.FINAL_KILLS, finalKills);
        put(statMap, StatType.FKDR, fkdr);

        int wins = JsonNullSafe.getInt(bedwars, "wins_bedwars");
        int losses = JsonNullSafe.getInt(bedwars, "losses_bedwars");
        float wlr = losses == 0 ? wins : ((float) wins / losses);

        put(statMap, StatType.WINS, wins);
        put(statMap, StatType.WLR, wlr);

        int bedsBroken = JsonNullSafe.getInt(bedwars, "beds_broken_bedwars");
        int bedsLost = JsonNullSafe.getInt(bedwars, "beds_lost_bedwars");
        float bblr = bedsLost == 0 ? bedsBroken : ((float) bedsBroken / bedsLost);

        put(statMap, StatType.BEDS_BROKEN, bedsBroken);
        put(statMap, StatType.BBLR, bblr);

        return statMap;
    }

    private static void put(Map<StatType, Data> map, StatType type, Number number) {
        map.put(type, new Data(number, type));
    }

    public Number getStatRaw(StatType type) {
        if (stats == null) return -1F;
        return stats.containsKey(type) ? stats.get(type).value : -1;
    }

    public String getStatFormatted(StatType type) {
        if (type == StatType.NAME) return getFormattedName();
        if (stats == null) return "";
        if (type == StatType.STARS && nicked) return "§c[NICK]";
        return stats.containsKey(type) ? stats.get(type).formatted : "";
    }

    public int getStatColor(StatType type) {
        if (stats == null) return 0xFFFFFF;
        return stats.containsKey(type) ? stats.get(type).color : 0xFFFFFF;
    }

    public boolean isStatsNull() {
        return stats == null;
    }

    public boolean isNicked() {
        return nicked;
    }

    public String getFormattedName() {
        if (UMinecraft.getWorld() == null) return name;
        String formatted = ScorePlayerTeam.formatPlayerName(UMinecraft.getWorld().getScoreboard().getPlayersTeam(name), isSelf ? OverlayConfig.selfName : name);
        if (OverlayConfig.prefixStars)
            formatted = getStatFormatted(StatType.STARS) + " §r" + formatted;
        return formatted;
    }

    public static class Data {
        public final Number value;
        public final String formatted;
        public final int color;

        public Data(Number value, StatType dataType) {
            this.value = value;
            this.formatted = dataType.format(value);
            this.color = dataType.getColor(value);
        }
    }

}
