package utils

import androidx.compose.ui.res.loadImageBitmap
import me.spste.common.utils.ImageLoader
import java.io.File

object DesktopImageLoader : ImageLoader(DesktopPropertyHandler,
    streamToImageBitmap = {
        loadImageBitmap(it)
    },
    stringToFile = {
        val resource = DesktopImageLoader.javaClass.classLoader.getResource(it)
        if (resource != null)
            File(resource.toURI())
        else {
            print("File $it not found in resources")
            null
        }

    }
) {

}