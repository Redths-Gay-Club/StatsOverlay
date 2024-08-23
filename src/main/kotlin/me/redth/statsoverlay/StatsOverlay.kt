package me.redth.statsoverlay

import cc.polyfrost.oneconfig.events.EventManager
import me.redth.statsoverlay.config.ModConfig
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

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

    @Mod.EventHandler
    fun onInit(e: FMLInitializationEvent) {
        ModConfig
        EventManager.INSTANCE.register(EventListener)
        MinecraftForge.EVENT_BUS.register(EventListener)
    }
}
