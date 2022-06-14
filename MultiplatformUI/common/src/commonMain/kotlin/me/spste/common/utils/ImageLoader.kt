package me.spste.common.utils

import androidx.compose.ui.graphics.ImageBitmap
import me.spste.common.mapsStaticAPI.ANALYSIS_OPTION
import me.spste.common.mapsStaticAPI.DISPLAY_OPTION
import me.spste.common.mapsStaticAPI.MapsService
import java.io.File
import java.io.InputStream


open class ImageLoader(private val propertyHandler: PropertiesHandler, val streamToImageBitmap: (InputStream) -> ImageBitmap?, val stringToStream: (String) -> InputStream?) {
    fun callMapImages(locationString: String): List<ImageBitmap?> {
        val analysisImage = loadAnalysisMapImage(locationString)
        val displayImage = loadDisplayMapImage(locationString)
        return listOf(analysisImage, displayImage)
    }

    private fun loadAnalysisMapImage(locationString: String): ImageBitmap? {
        var bitmap: ImageBitmap? = null
        val stream = MapsService(propertyHandler).getMap(locationString, ANALYSIS_OPTION)?.byteStream()
        bitmap = stream?.let { streamToImageBitmap(it) }
        return bitmap
    }

    private fun loadDisplayMapImage(locationString: String): ImageBitmap? {
        var bitmap: ImageBitmap? = null
        val stream = MapsService(propertyHandler).getMap(locationString, DISPLAY_OPTION)?.byteStream()
        bitmap = stream?.let { streamToImageBitmap(it) }
        return bitmap
    }

    fun loadImage(fileName: String): ImageBitmap? {
        val stream = stringToStream(fileName)
        return if (stream != null)
            streamToImageBitmap(stream)
        else null

    }
}

