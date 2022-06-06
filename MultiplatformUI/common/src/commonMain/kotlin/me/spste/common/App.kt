package me.spste.common

import ANALYSIS_FILE
import DISPLAY_FILE
import androidx.compose.ui.graphics.ImageBitmap
import mapCall
import me.spste.common.utils.PropertiesHandler

fun App(loadImageBitmap: (fileLocation: String) -> ImageBitmap, propertiesHandler: PropertiesHandler) {
    var locationString: String? = "37,-2"
    var maps = locationString?.let { mapCall(it, propertiesHandler) }
    val analysisImage = maps?.get(ANALYSIS_FILE)?.path?.let { loadImageBitmap(it) }
    val displayImage = maps?.get(DISPLAY_FILE)?.path?.let { loadImageBitmap(it) }
    val model = FFModel(analysisImage, propertiesHandler)


}