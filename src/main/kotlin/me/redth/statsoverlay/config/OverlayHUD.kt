package me.redth.statsoverlay.config

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.hud.BasicHud
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.redth.statsoverlay.EventListener
import me.redth.statsoverlay.data.Alignment
import me.redth.statsoverlay.data.CompiledGrid
import me.redth.statsoverlay.data.ColoredText
import me.redth.statsoverlay.data.FetchQueue
import net.minecraft.client.renderer.GlStateManager

private const val ROW_HEIGHT = 10

class OverlayHUD : BasicHud(true) {
    @Transient
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

        var yNow = 0
        for (row in grid.grid) {
            var xNow = 0
            for ((index, text) in row.withIndex()) {
                val width = grid.widthMap[index]
                drawText(text, xNow, yNow, width)
                xNow += width
            }
            yNow += ROW_HEIGHT
        }


        GlStateManager.popMatrix()
    }

    override fun getWidth(scale: Float, example: Boolean) = (compiledGrid?.width?.toFloat() ?: 0f) * scale
    override fun getHeight(scale: Float, example: Boolean) = (compiledGrid?.height?.toFloat() ?: 0f) * scale

    override fun shouldShow() = super.shouldShow() && EventListener.shouldShow()

    private fun drawText(text: ColoredText, x: Int, y: Int, width: Int) {
        val offset = when (text.alignment) {
            Alignment.LEFT -> 0f
            Alignment.CENTER -> width / 2f - text.width / 2f
            Alignment.RIGHT -> width.toFloat() - text.width.toFloat()
        }
        mc.fontRendererObj.drawString(text.text, x.toFloat() + offset, y.toFloat(), text.color, true)
    }

}
