package me.spste.android.androidUtils

import android.content.Context
import me.spste.android.R
import java.io.File
import java.util.ResourceBundle

class PropertyHandler() {
    val keyMap = mutableMapOf<String, String>()
    init {
        keyMap["api_key"] = "AIzaSyCQLBz2ehk-eEZO8bt4uWs9KUYYoPTCtyg"
        keyMap["default_zoom"] = "15"
        keyMap["default_size"] = "480x480"
        keyMap["format"] = "png32"
        keyMap["maptype"] = "roadmap"
        keyMap["style_analysis"] = "&style=element:labels%7Cvisibility:off&style=feature:administrative%7Cvisibility:off&style=feature:landscape%7Celement:geometry%7Ccolor:0x2fa745&style=feature:poi%7Cvisibility:off&style=feature:road%7Ccolor:0xffed4d&style=feature:transit%7Ccolor:0xffed4d&style=feature:transit.line%7Cvisibility:off&style=feature:water%7Celement:geometry%7Ccolor:0x5eb3e8\n"
        keyMap["style_display"] = "&style=feature:landscape%7Celement:labels%7Cvisibility:off&style=feature:poi%7Celement:labels%7Cvisibility:off&style=feature:road%7Celement:labels%7Cvisibility:off&style=feature:transit%7Celement:labels%7Cvisibility:off&style=feature:water%7Celement:labels%7Cvisibility:off"
        keyMap["water"] = "2"
        keyMap["road"] = "1"
        keyMap["green"] = "0"
    }


    fun getLocalProperty(key: String, filename: String = "notNeeded"): String? {
        return keyMap[key]
    }
}