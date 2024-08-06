package me.redth.statsoverlay

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.*
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.utils.dsl.mc
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo
import me.redth.statsoverlay.config.ModConfig
import me.redth.statsoverlay.data.FetchQueue
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent

@Mod(
    modid = StatsOverlay.MODID,
    name = StatsOverlay.NAME,
    version = StatsOverlay.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object StatsOverlay {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    var inBedwars = false
    val inAllowedArea get() = ModConfig.showAnywhere || inBedwars

    @Mod.EventHandler
    fun onInit(e: FMLInitializationEvent) {
        ModConfig
        EventManager.INSTANCE.register(this)
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Subscribe
    fun onPacketReceived(event: ReceivePacketEvent) {
        if (!inAllowedArea) return
        val packetPlayerList = event.packet as? S38PacketPlayerListItem ?: return

        when (packetPlayerList.action) {
            S38PacketPlayerListItem.Action.ADD_PLAYER -> {
                for (entry in packetPlayerList.entries) {
                    FetchQueue.add(entry.profile)
                }
            }

            S38PacketPlayerListItem.Action.REMOVE_PLAYER -> {
                for (entry in packetPlayerList.entries) {
                    FetchQueue.remove(entry.profile)
                }
            }

            else -> {}
        }
    }

    @Subscribe
    fun onWorldLoad(event: WorldLoadEvent) {
        FetchQueue.clear()
        inBedwars = false
    }

    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent) {
        FetchQueue.clear()
        inBedwars = false
    }

    @Subscribe
    fun onLocraw(event: LocrawEvent) {
        if (ModConfig.showAnywhere) return

        inBedwars = event.info.gameType == LocrawInfo.GameType.BEDWARS && event.info.gameMode != "lobby"

        if (!inBedwars) return

        for (networkPlayerInfo in mc.thePlayer.sendQueue.playerInfoMap) {
            FetchQueue.add(networkPlayerInfo.gameProfile)
        }
    }
}
