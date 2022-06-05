package me.spste.common.utils

import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.currentCompositionLocalContext
import me.spste.common.getPlatformName
import java.io.File

fun getLocalProperty(key: String, file: String = "local.properties"): String {
    val properties = java.util.Properties()
    if (getPlatformName() == "Android") {
        //todo
        return "todo"
    }else {
        val localProperties = File(file)
        if (localProperties.isFile) {
            java.io.InputStreamReader(java.io.FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
        } else error("File $file not found")

        return properties.getProperty(key)
    }

}