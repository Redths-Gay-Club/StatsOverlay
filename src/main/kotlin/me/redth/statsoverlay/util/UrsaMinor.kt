package me.redth.statsoverlay.util

import cc.polyfrost.oneconfig.utils.dsl.mc
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object UrsaMinor {
    private var tokenState: TokenState = None

    private fun getUrl(uuid: UUID) = URL("https://api.polyfrost.cc/ursa/v1/hypixel/player/${uuid.toString().replace("-", "")}")

    fun setupConnection(uuid: UUID) = (getUrl(uuid).openConnection() as HttpURLConnection).apply {
        requestMethod = "GET"
        useCaches = false
        addRequestProperty("User-Agent", "StatsOverlay :>")
        readTimeout = 5000
        connectTimeout = 5000
        doOutput = true

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
    }.inputStream.bufferedReader()

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
        private val expiryInMillis = try {
            expiry?.toLong() ?: (System.currentTimeMillis() + FIFTY_FIVE_MINUTES)
        } catch (_: Exception) {
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