package me.spste.common.utils


import ROAD
import TREE
import WATER
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap


fun generateMapFromImage(img: ImageBitmap) : MutableList<MutableList<Int>> {
    val valuesMap = mutableMapOf<Int, FloatArray>()
    val pixelMap = img.toPixelMap()
    val intMap = mutableListOf<MutableList<Int>>()
    val waterValue = getLocalProperty("water", "simulation.properties")
    val roadValue = getLocalProperty("road", "simulation.properties")
    val greenValue = getLocalProperty("green", "simulation.properties")
    for(i in 0 until img.width) {
        intMap.add(mutableListOf())
        for (j in 0 until img.height) {
            intMap[i].add(when(pixelMap[i, j].toArgb()) {
                -4787 -> ROAD
                -10570776 -> WATER
                else -> TREE
            })
        }
    }
    return intMap
}