package me.redth.statsoverlay.util

import cc.polyfrost.oneconfig.utils.dsl.mc
import com.google.gson.Gson
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.data.PlayerResponse
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

object HypixelAPI {
    private val gson = Gson()
    private var tokenState: TokenState = None

    fun getPlayerResponse(uuid: UUID): PlayerResponse? = getResponse(getPlayerURL(uuid))

    private fun getPlayerURL(uuid: UUID) = if (ModConfig.useHypixelAPI)
        "https://api.hypixel.net/player?uuid=$uuid"
    else
        "https://api.polyfrost.cc/ursa/v1/hypixel/player/${uuid.toString().replace("-", "")}"

    inline fun <reified T> getResponse(url: String) = getResponse(url, T::class.java)

    fun <T> getResponse(url: String, clazz: Class<T>) = runCatching {
        gson.fromJson(readConnectionSync(url), clazz)
    }.onFailure { exception ->
        exception.printStackTrace()
    }.getOrNull()

    private fun readConnectionSync(url: String) = synchronized(this) {
        readConnection(url)
    }

    private fun readConnection(url: String) = setupConnection(url).inputStream.bufferedReader()

    private fun setupConnection(url: String) = (URL(url).openConnection() as HttpURLConnection).apply {
        requestMethod = "GET"
        useCaches = false
        addRequestProperty("User-Agent", "Hytils-Reborn/1.6.0-beta5alpha1")
        readTimeout = 5000
        connectTimeout = 5000
        doOutput = true

        if (ModConfig.useHypixelAPI) {
            addRequestProperty("API-Key", ModConfig.hypixelApiKey)
            return@apply
        }

        val token = tokenState.token

        if (token != null) {
            addRequestProperty("x-ursa-token", token)
        } else {
            val requesting = authorizeNewServer()
            addRequestProperty("x-ursa-username", requesting.username)
            addRequestProperty("x-ursa-serverid", requesting.serverId)
            tokenState = requesting

            tokenState = Session(
                tokenString = getHeaderField("x-ursa-token"),
                expiry = getHeaderField("x-ursa-expires")
            )
        }
    }

    private fun authorizeNewServer(): Requesting {
        val newServerId = UUID.randomUUID().toString()
        val profile = mc.session.profile
        val token = mc.session.token

        mc.sessionService.joinServer(profile, token, newServerId)

        return Requesting(profile.name, newServerId)
    }

    const val FIFTY_FIVE_MINUTES = 55 * 60 * 1000


    private interface TokenState {
        val token: String?
    }

    private object None : TokenState {
        override val token = null
    }

    private class Session(
        private val tokenString: String?,
        expiry: String?
    ) : TokenState {
        private val expiryInMillis = runCatching {
            expiry?.toLong() ?: (System.currentTimeMillis() + FIFTY_FIVE_MINUTES)
        }.getOrElse {
            System.currentTimeMillis() + FIFTY_FIVE_MINUTES
        }

        override val token
            get() = tokenString.takeIf {
                expiryInMillis > System.currentTimeMillis()
            }
    }

    private class Requesting(
        val username: String,
        val serverId: String
    ) : TokenState {
        override val token = null
    }
}