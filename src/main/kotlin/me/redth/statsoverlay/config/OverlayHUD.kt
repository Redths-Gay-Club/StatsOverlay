package me.redth.statsoverlay.config

import cc.polyfrost.oneconfig.hud.BasicHud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.redth.statsoverlay.StatsOverlay
import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.stats.StatsType
import me.redth.statsoverlay.data.stats.StatsTypes
import me.redth.statsoverlay.data.statsmap.StatsMaps
import net.minecraft.client.renderer.GlStateManager

class OverlayHUD : BasicHud(true) {
    private val widthMap = HashMap<StatsType, Int>()
    private var width = 0
    private var height = 0

    override fun preRender(example: Boolean) {
        width = 0
        for (type in StatsTypes.ALL) {
            if (!type.shouldShow()) continue
            val typeWidth = calculateWidth(type)
            widthMap[type] = typeWidth
            width += typeWidth
        }

        height = StatsMaps.sortedStatsMap.size * 10 + 8
    }

    private fun calculateWidth(type: StatsType) = StatsMaps.sortedStatsMap
        .map { statsMap ->
            statsMap[type]
        }
        .plus(ColoredText(type.title))
        .maxOf { text ->
            text.width + 4
        }

    override fun draw(matrices: UMatrixStack, x: Float, y: Float, scale: Float, example: Boolean) {
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, 0f)
        GlStateManager.scale(scale, scale, 1f)

        var x1 = 0
        for (type in StatsTypes.ALL) {
            if (!type.shouldShow()) continue
            val width = widthMap[type] ?: continue

            drawCenteredText(ColoredText(type.title), (x1 + width / 2).toFloat(), 0f)

            var y1 = 10

            for (stats in StatsMaps.sortedStatsMap) {
                drawRightedText(stats[type], (x1 + width - 2).toFloat(), y1.toFloat())
                y1 += 10
            }

            x1 += width
        }

        GlStateManager.popMatrix()
    }

    override fun getWidth(scale: Float, example: Boolean) = width * scale

    override fun getHeight(scale: Float, example: Boolean) = height * scale

    override fun shouldShow() = super.shouldShow() && StatsOverlay.inAllowedPlace && ModConfig.isKeyPressed

    private fun drawCenteredText(text: ColoredText, x: Float, y: Float) =
        mc.fontRendererObj.drawString(text.text, x - text.width / 2.0f, y, text.color, true)

    private fun drawRightedText(text: ColoredText, x: Float, y: Float) =
        mc.fontRendererObj.drawString(text.text, x - text.width, y, text.color, true)

    private fun drawText(text: ColoredText, x: Float, y: Float) =
        mc.fontRendererObj.drawString(text.text, x, y, text.color, true)

}
