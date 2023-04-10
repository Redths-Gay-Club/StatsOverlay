package me.redth.statsoverlay.stat;

import cc.polyfrost.oneconfig.libs.caffeine.cache.Cache;
import cc.polyfrost.oneconfig.libs.caffeine.cache.Caffeine;
import com.mojang.authlib.GameProfile;
import me.redth.statsoverlay.config.OverlayConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataList {
    public static final Cache<UUID, Map<StatType, PlayerData.Data>> CACHE = Caffeine.newBuilder().initialCapacity(16).maximumSize(100).build();
    private static final Map<UUID, PlayerData> LIST = Collections.synchronizedMap(new HashMap<>());
    public static boolean altered;

    public static void add(GameProfile profile) {
        if (LIST.size() > 32) return;
        UUID uuid = profile.getId();
        if (uuid.version() == 2) return;
        if (LIST.containsKey(uuid)) return;

        LIST.put(uuid, new PlayerData(profile));

        altered = true;
    }

    public static void remove(GameProfile profile) {
        UUID uuid = profile.getId();
        if (uuid.version() == 2) return;
        LIST.remove(uuid);
        altered = true;
    }

    public static void clear() {
        if (LIST.isEmpty()) return;
        LIST.clear();
        altered = true;
    }

    public static void update(List<PlayerData> list) {
        list.clear();
        list.addAll(LIST.values());
        list.sort(StatType.SORTABLE[OverlayConfig.sorting]);
    }
}
