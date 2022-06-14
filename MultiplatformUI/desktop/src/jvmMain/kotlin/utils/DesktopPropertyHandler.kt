package utils

import me.spste.common.getPlatformName
import me.spste.common.utils.PropertiesHandler
import java.io.File

object DesktopPropertyHandler : PropertiesHandler {
    override fun getLocalProperty(key: String, filename: String): String {
        val properties = java.util.Properties()
        val resource = DesktopPropertyHandler.javaClass.classLoader.getResourceAsStream(filename)
        if (resource != null) {
            java.io.InputStreamReader(resource, Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
        } else error("File $filename not found")
        return properties.getProperty(key)
    }

    override fun getLocalProperty(key: String): Any {
        val properties = java.util.Properties()

        val localProperties = File("local.properties")
        if (localProperties.isFile) {
            java.io.InputStreamReader(java.io.FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
        } else error("File local.properties not found")

        return properties.getProperty(key)
    }
}