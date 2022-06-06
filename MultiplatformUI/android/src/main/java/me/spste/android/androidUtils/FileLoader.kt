package me.spste.android.androidUtils

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.*

class FileLoader(val context: Context) {
    fun loadImageBitmap(filename: String) : ImageBitmap {
        return BitmapFactory.decodeFile(filename).asImageBitmap()
    }

    fun writeFile(sFileName: String, directoryName:String = "temp", sBody: ByteArray) {
        val dir = File(context.filesDir, directoryName)
        if (!dir.exists()) {
            dir.mkdir()
        }
        try {
            val gpxfile = File(dir, sFileName)
            val stream = gpxfile.outputStream()
            val buffer = BufferedOutputStream(stream)
            buffer.write(sBody)
            buffer.flush()
            buffer.close()
        } catch (e: Exception) {
            error("Could not write file $sFileName")
        }
    }

    fun readImageBitmap(sFileName: String, directoryName:String = "temp"): ImageBitmap {
        val dir: File = File(context.filesDir, directoryName)
        val path =  File(dir, sFileName)
        return BitmapFactory.decodeFile(path.absolutePath).asImageBitmap()
    }

    fun readFile(s: String, directoryName: String = "temp"): String {
        val dir = File(context.filesDir, directoryName)
        var fileBytes = ""
        if (dir.exists()) {
            val file = File(dir, s)
            val reader = FileReader(file)
            while(reader.ready()) {
                fileBytes += reader.read()
            }
        }
        return fileBytes
    }
}