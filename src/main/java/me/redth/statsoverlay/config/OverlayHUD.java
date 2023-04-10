package me.redth.statsoverlay.config;

import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.platform.Platform;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import me.redth.statsoverlay.StatsOverlay;
import me.redth.statsoverlay.stat.DataList;
import me.redth.statsoverlay.stat.PlayerData;
import me.redth.statsoverlay.stat.StatType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverlayHUD extends BasicHud {
    private final List<PlayerData> display = new ArrayList<>();
    private final Map<StatType, Integer> widthMap = new HashMap<>();

    public OverlayHUD() {
        super(true);
    }

    @Override
    protected void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {
        UGraphics.GL.pushMatrix();
        UGraphics.GL.translate(x, y, 0);
        UGraphics.GL.scale(scale, scale, 1);

        int x1 = 0;

        for (StatType type : StatType.values()) {
            if (!type.shouldShow()) continue;
            int width = widthMap.get(type);

            drawText(type.name, x1 + 2, 0, 0xFFFFFFFF);

            int y1 = 10;
            for (PlayerData stats : display) {
                if (type.rightAlign)
                    drawRightedText(stats.getStatFormatted(type), x1 + width - 2, y1, stats.getStatColor(type));
                else
                    drawText(stats.getStatFormatted(type), x1 + 2, y1, stats.getStatColor(type));
                y1 += 10;
            }
            x1 += width;
        }

        UGraphics.GL.popMatrix();
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        if (widthMap == null) return 0F;

        if (DataList.altered) {
            update();
            DataList.altered = false;
        }

        float cacheWidth = 0;

        for (StatType type : StatType.values()) {
            if (!type.shouldShow()) continue;
            cacheWidth += widthMap.get(type);
        }

        return cacheWidth * scale;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        if (display == null) return 0F;

        return (display.size() * 10 + 8) * scale;
    }

    private void update() {
        DataList.update(display);

        for (StatType type : StatType.values()) {
            widthMap.put(type, calculateWidth(type));
        }
    }

    private int calculateWidth(StatType type) {
        int width = Platform.getGLPlatform().getStringWidth(type.name);
        for (PlayerData stats : display) {
            width = Math.max(width, Platform.getGLPlatform().getStringWidth(stats.getStatFormatted(type)));
        }
        return width + 4;
    }

    @Override
    protected boolean shouldShow() {
        return super.shouldShow() && (OverlayConfig.showAnyWhere || (HypixelUtils.INSTANCE.isHypixel() && StatsOverlay.inBW)) && OverlayConfig.isKeyPressed();
    }

    private void drawCenteredText(String text, float x, float y, int color) {
        int width = Platform.getGLPlatform().getStringWidth(text);
        Platform.getGLPlatform().drawText(text, x - width / 2.0F, y, color, true);
    }

    private void drawRightedText(String text, float x, float y, int color) {
        int width = Platform.getGLPlatform().getStringWidth(text);
        Platform.getGLPlatform().drawText(text, x - width, y, color, true);
    }

    private void drawText(String text, float x, float y, int color) {
        Platform.getGLPlatform().drawText(text, x, y, color, true);
    }
}
