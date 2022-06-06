package utils

import me.spste.common.getPlatformName
import me.spste.common.utils.PropertiesHandler
import java.io.File

class DesktopPropertyHandler : PropertiesHandler {
    override fun getLocalProperty(key: String, filename: String): String {
        val properties = java.util.Properties()
        if (getPlatformName() == "Android") {
            //todo
            return "todo"
        }else {
            val localProperties = File(filename)
            if (localProperties.isFile) {
                java.io.InputStreamReader(java.io.FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                    properties.load(reader)
                }
            } else error("File $filename not found")

            return properties.getProperty(key)
        }

    }
}