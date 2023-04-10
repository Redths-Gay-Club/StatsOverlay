package me.redth.statsoverlay.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.DualOption;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.HypixelKey;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import me.redth.statsoverlay.stat.DataList;
import org.lwjgl.input.Keyboard;

public class OverlayConfig extends Config {

    @KeyBind(name = "Overlay Key Bind")
    public static OneKeyBind keyBind = new OneKeyBind(Keyboard.KEY_TAB);

    @DualOption(name = "Key Bind Mode", left = "Hold", right = "Toggle")
    public static boolean toggleKeyBind = false;

    @DualOption(name = "Show in", left = "Only in Bedwars", right = "Any Server")
    public static boolean showAnyWhere = false;

    @Dropdown(name = "Sorting", options = {"Stars", "Final Kills", "FKDR", "Wins", "WLR", "Beds Broken", "BBLR"})
    public static int sorting = 0;

    @Text(name = "Self Name")
    public static String selfName = "§cYou";

    @Switch(name = "Show Stars Before Name")
    public static boolean prefixStars = true;

    @Checkbox(name = "Final Kills")
    public static boolean finalKills = true;

    @Checkbox(name = "FKDR")
    public static boolean fkdr = true;

    @Checkbox(name = "Wins")
    public static boolean wins = true;

    @Checkbox(name = "WLR")
    public static boolean wlr = true;

    @Checkbox(name = "Beds Broken")
    public static boolean bedsBroken = true;

    @Checkbox(name = "BBLR")
    public static boolean bblr = true;

    @HUD(name = "Overlay")
    public static OverlayHUD hud = new OverlayHUD();

    @HypixelKey()
    @Text(name = "Hypixel API key", secure = true, category = "Hypixel API")
    public static String apiKey;

    private static boolean keyBindToggled;
    private static boolean keyPressed;

    public OverlayConfig() {
        super(new Mod("StatsOverlay", ModType.HYPIXEL), "statsoverlay.json");
        initialize();
        addListener("sorting", () -> DataList.altered = true);
        addListener("keyBindMode", () -> keyBindToggled = false);
    }

    public static boolean isKeyPressed() {
        if (toggleKeyBind) {
            if (keyBind.isActive()) {
                if (!keyPressed) {
                    keyPressed = true;
                    keyBindToggled = !keyBindToggled;
                }
            } else {
                keyPressed = false;
            }
            return keyBindToggled;
        }

        return keyBind.isActive();
    }
}