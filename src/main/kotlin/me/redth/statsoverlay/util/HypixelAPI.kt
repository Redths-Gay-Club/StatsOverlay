package me.redth.statsoverlay.util

import com.google.gson.Gson
import me.redth.statsoverlay.config.ModConfig
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object HypixelAPI {
    private val gson = Gson()

    fun getPlayerResponse(uuid: UUID) = getResponse(uuid, PlayerResponse::class.java)

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