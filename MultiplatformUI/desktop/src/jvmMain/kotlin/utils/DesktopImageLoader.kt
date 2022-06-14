package utils

import androidx.compose.ui.res.loadImageBitmap
import me.spste.common.utils.ImageLoader
import java.io.File

object DesktopImageLoader : ImageLoader(DesktopPropertyHandler,
    streamToImageBitmap = {
        loadImageBitmap(it)
    },
    stringToStream = {
        val resource = DesktopImageLoader.javaClass.classLoader.getResourceAsStream(it)
        if (resource != null)
            resource
        else {
            print("File $it not found in resources")
            null
        }

    }
) {

}