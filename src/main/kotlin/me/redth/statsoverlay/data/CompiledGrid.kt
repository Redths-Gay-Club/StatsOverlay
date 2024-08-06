package me.redth.statsoverlay.data

data class CompiledGrid(
    val grid: List<List<ColoredText>>,
    val widthMap: List<Int>,
    val width: Int,
    val height: Int,
)
