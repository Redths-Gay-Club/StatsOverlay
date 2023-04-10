package me.redth.statsoverlay.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonNullSafe {
    public static int getInt(JsonObject object, String key) {
        if (object == null) return 0;
        JsonElement element = object.get(key);
        return isNull(element) ? 0 : element.getAsInt();
    }

    public static boolean getBoolean(JsonObject object, String key) {
        if (object == null) return false;
        JsonElement element = object.get(key);
        return !isNull(element) && element.getAsBoolean();
    }

    public static JsonObject getAsObject(JsonElement element) {
        return isNull(element) ? null : element.getAsJsonObject();
    }

    public static JsonObject getObject(JsonObject object, String key) {
        if (object == null) return null;
        JsonElement element = object.get(key);
        return isNull(element) ? null : element.getAsJsonObject();
    }

    public static boolean isNull(JsonElement element) {
        return element == null || element.isJsonNull();
    }
}
