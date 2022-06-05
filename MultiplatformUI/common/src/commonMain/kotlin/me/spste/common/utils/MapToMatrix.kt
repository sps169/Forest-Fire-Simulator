package me.spste.common.utils

import ROAD
import TREE
import WATER
import androidx.compose.ui.graphics.ImageBitmap
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
            val pixel = pixelMap[i, j]
            intMap[i].add(when(RGBtoHSB(pixel.red.toDouble(), pixel.green.toDouble(), pixel.blue.toDouble())[0]) {
                in 30f..75f -> ROAD
                in 75f.. 150f -> TREE
                in 150f..270f -> WATER
                else -> TREE
            })
        }
    }

    return intMap
}

fun RGBtoHSB(r: Double, g: Double, b: Double): FloatArray {
    val cmax = Math.max(r, Math.max(g, b)) // maximum of r, g, b
    val cmin = Math.min(r, Math.min(g, b)) // minimum of r, g, b
    val diff = cmax - cmin // diff of cmax and cmin.
    var h = -1.0
    var s = -1.0

    // if cmax and cmax are equal then h = 0
    if (cmax == cmin) h = 0.0 else if (cmax == r) h = (60 * ((g - b) / diff) + 360) % 360 else if (cmax == g) h =
        (60 * ((b - r) / diff) + 120) % 360 else if (cmax == b) h = (60 * ((r - g) / diff) + 240) % 360

    // if cmax equal zero
    s = if (cmax == 0.0) 0.0 else diff / cmax * 100

    // compute v
    val v = cmax * 100
    return floatArrayOf(h.toFloat(), s.toFloat(), v.toFloat())
}