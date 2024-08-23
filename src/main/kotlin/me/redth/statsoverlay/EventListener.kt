package me.redth.statsoverlay

import cc.polyfrost.oneconfig.events.event.LocrawEvent
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.events.event.WorldLoadEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.utils.dsl.mc
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo
import com.mojang.authlib.GameProfile
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.data.FetchQueue
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object EventListener {
    fun shouldShow() = isInAllowedArea() && isKeyPressed()

    private var wasInBedwars = false
    private var keyBindToggled = false
    private var lastKeyBindActive = false

    private fun isInAllowedArea() = ModConfig.showAnywhere || wasInBedwars
    private fun isKeyPressed(): Boolean {
        if (!ModConfig.toggleKeyBind) return ModConfig.keyBind.isActive

        if (lastKeyBindActive != ModConfig.keyBind.isActive) {
            lastKeyBindActive = ModConfig.keyBind.isActive
            if (lastKeyBindActive) keyBindToggled = !keyBindToggled
        }

        return keyBindToggled
    }

    private fun GameProfile.isNPC() = id.version() == 2

    @Subscribe
    fun onPacketReceived(event: ReceivePacketEvent) {
        if (!isInAllowedArea()) return
        val packetPlayerList = event.packet as? S38PacketPlayerListItem ?: return

        when (packetPlayerList.action) {
            S38PacketPlayerListItem.Action.ADD_PLAYER -> {
                for (entry in packetPlayerList.entries) {
                    if (entry.profile.isNPC()) continue
                    FetchQueue.add(entry.profile)
                }
            }

            S38PacketPlayerListItem.Action.REMOVE_PLAYER -> {
                for (entry in packetPlayerList.entries) {
                    if (entry.profile.isNPC()) continue
                    FetchQueue.remove(entry.profile)
                }
            }

            else -> {}
        }
    }

    @Subscribe
    fun onWorldLoad(event: WorldLoadEvent) {
        FetchQueue.clear()
        wasInBedwars = false
    }

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        FetchQueue.clear()
        wasInBedwars = false
    }

    @Subscribe
    fun onLocraw(event: LocrawEvent) {
        if (ModConfig.showAnywhere) return

        val nowInBw = event.info.gameType == LocrawInfo.GameType.BEDWARS && event.info.gameMode != "lobby"
        if (wasInBedwars == nowInBw) return
        wasInBedwars = nowInBw
        if (!nowInBw) return

        for (networkPlayerInfo in mc.thePlayer.sendQueue.playerInfoMap) {
            if (networkPlayerInfo.gameProfile.isNPC()) continue
            FetchQueue.add(networkPlayerInfo.gameProfile)
        }
    }
}