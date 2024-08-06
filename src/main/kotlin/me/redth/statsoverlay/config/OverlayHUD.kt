package me.redth.statsoverlay.config

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.hud.BasicHud
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.redth.statsoverlay.StatsOverlay
import me.redth.statsoverlay.data.CompiledGrid
import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.FetchQueue
import net.minecraft.client.renderer.GlStateManager

private const val ROW_HEIGHT = 10

class OverlayHUD : BasicHud(true) {
    private var compiledGrid: CompiledGrid? = null

    init {
        EventManager.INSTANCE.register(this)
    }

    @Subscribe
    fun onTick(event: TickEvent) {
        if (event.stage != Stage.END) return
        compiledGrid = FetchQueue.makeGrid()
    }

    override fun draw(matrices: UMatrixStack, x: Float, y: Float, scale: Float, example: Boolean) {
        val grid = compiledGrid ?: return

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, 0f)
        GlStateManager.scale(scale, scale, 1f)

        var xNow = 0
        var yNow = 0

        for (row in grid.grid) {
            for ((index, text) in row.withIndex()) {
                val width = grid.widthMap[index]
                drawCenteredText(text, xNow + width / 2f, yNow.toFloat())
                xNow += width
            }
            yNow += ROW_HEIGHT
        }


        GlStateManager.popMatrix()
    }

    override fun getWidth(scale: Float, example: Boolean) = (compiledGrid?.width?.toFloat() ?: 0f) * scale
    override fun getHeight(scale: Float, example: Boolean) = (compiledGrid?.height?.toFloat() ?: 0f) * scale

    override fun shouldShow() = super.shouldShow() && StatsOverlay.inAllowedArea && ModConfig.isKeyPressed

    private fun drawCenteredText(text: ColoredText, x: Float, y: Float) =
        mc.fontRendererObj.drawString(text.text, x - text.width / 2.0f, y, text.color, true)

    private fun drawRightAlignedText(text: ColoredText, x: Float, y: Float) =
        mc.fontRendererObj.drawString(text.text, x - text.width, y, text.color, true)

    private fun drawText(text: ColoredText, x: Float, y: Float) =
        mc.fontRendererObj.drawString(text.text, x, y, text.color, true)

}
