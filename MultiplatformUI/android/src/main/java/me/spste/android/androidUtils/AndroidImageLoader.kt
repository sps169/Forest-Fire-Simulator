package me.spste.android.androidUtils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import me.spste.common.utils.ImageLoader
import java.io.File

object AndroidImageLoader : ImageLoader(
    AndroidPropertyHandler,
    streamToImageBitmap = {
        BitmapFactory.decodeStream(it).asImageBitmap()
    },
    stringToStream = {
        val resource = AndroidImageLoader.javaClass.classLoader.getResourceAsStream(it)
        if (resource != null)
            resource
        else {
            print("File $it not found in resources")
            null
        }
    }
)