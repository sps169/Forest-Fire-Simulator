package me.spste.android.androidUtils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.spste.common.FFModel
import me.spste.common.mapsStaticAPI.ANALYSIS_OPTION
import me.spste.common.mapsStaticAPI.DISPLAY_OPTION
import me.spste.common.mapsStaticAPI.MapsService


class ImageLoader() {
    fun callImages(locationString: String): List<ImageBitmap?> {
            val analysisImage = loadAnalysisMapImage(locationString)
            val displayImage = loadDisplayMapImage(locationString)
            return listOf(analysisImage, displayImage)
    }

    fun loadAnalysisMapImage(locationString: String): ImageBitmap? {
        var bitmap: ImageBitmap? = null
        val stream = MapsService(AndroidPropertyHandler).getMap(locationString, ANALYSIS_OPTION)?.byteStream()
        bitmap = BitmapFactory.decodeStream(stream).asImageBitmap()
        return bitmap
    }

    fun loadDisplayMapImage(locationString: String): ImageBitmap? {
        var bitmap: ImageBitmap? = null
        val stream = MapsService(AndroidPropertyHandler).getMap(locationString, DISPLAY_OPTION)?.byteStream()
        bitmap = BitmapFactory.decodeStream(stream).asImageBitmap()
        return bitmap
    }
}

