package me.redth.statsoverlay.util

import cc.polyfrost.oneconfig.libs.caffeine.cache.Caffeine
import cc.polyfrost.oneconfig.libs.caffeine.cache.LoadingCache
import com.google.gson.Gson
import me.redth.statsoverlay.config.ModConfig
import java.net.HttpURLConnection
import java.net.URL
import java.time.Duration
import java.util.*


object HypixelAPI {
    private val gson = Gson()
    private var cache: LoadingCache<UUID, PlayerResponse?> = Caffeine.newBuilder()
        .maximumSize(10000)
        .expireAfterAccess(Duration.ofMinutes(5))
        .refreshAfterWrite(Duration.ofMinutes(10))
        .build { uuid -> fetchPlayerResponse(uuid) }

    fun clearCache() {
        cache.invalidateAll()
    }

    fun getStatsOrCached(uuid: UUID) = cache[uuid]

    private fun fetchPlayerResponse(uuid: UUID) = getResponse(uuid, PlayerResponse::class.java)

    private fun <T> getResponse(uuid: UUID, clazz: Class<T>): T? = runCatching {
        val reader = when {
            ModConfig.useHypixelAPI -> setupConnection(uuid)
            else -> UrsaMinor.setupConnection(uuid)
        }
        gson.fromJson(reader, clazz)
    }.onFailure { exception ->
        exception.printStackTrace()
    }.getOrNull()

    private fun setupConnection(uuid: UUID) =
        (URL("https://api.hypixel.net/player?uuid=$uuid").openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            useCaches = false
            readTimeout = 5000
            connectTimeout = 5000
            doOutput = true
            addRequestProperty("User-Agent", "StatsOverlay :>")
            addRequestProperty("API-Key", ModConfig.hypixelApiKey)
        }.inputStream.bufferedReader()

}