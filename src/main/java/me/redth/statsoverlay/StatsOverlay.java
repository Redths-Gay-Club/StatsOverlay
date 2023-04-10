package me.redth.statsoverlay;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.LocrawEvent;
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.events.event.WorldLoadEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import me.redth.statsoverlay.config.OverlayConfig;
import me.redth.statsoverlay.stat.DataList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = "@ID@", name = "@NAME@", version = "@VER@")
public class StatsOverlay {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static OverlayConfig config;
    public static boolean inBW;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        config = new OverlayConfig();
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    public void onPacketReceived(ReceivePacketEvent event) {
        if (!(event.packet instanceof S38PacketPlayerListItem)) return;
        if (!OverlayConfig.showAnyWhere && !inBW) return;

        S38PacketPlayerListItem.Action action = ((S38PacketPlayerListItem) event.packet).getAction();
        if (action == S38PacketPlayerListItem.Action.ADD_PLAYER) {
            for (S38PacketPlayerListItem.AddPlayerData entry : ((S38PacketPlayerListItem) event.packet).getEntries()) {
                DataList.add(entry.getProfile());
            }
        } else if (action == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
            for (S38PacketPlayerListItem.AddPlayerData entry : ((S38PacketPlayerListItem) event.packet).getEntries()) {
                DataList.remove(entry.getProfile());
            }
        }
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent event) {
        DataList.clear();
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        DataList.clear();
    }

    @Subscribe
    public void onLocraw(LocrawEvent event) {
        if (OverlayConfig.showAnyWhere) return;
        inBW = event.info.getGameType() == LocrawInfo.GameType.BEDWARS && !"lobby".equals(event.info.getGameMode());
        if (inBW) {
            for (NetworkPlayerInfo networkPlayerInfo : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
                DataList.add(networkPlayerInfo.getGameProfile());
            }
        }
    }


    // todo: align
}
