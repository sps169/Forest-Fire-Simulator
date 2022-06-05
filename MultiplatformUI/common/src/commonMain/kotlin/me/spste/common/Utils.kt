import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame
import me.spste.common.mapsStaticAPI.MapsService
import okhttp3.ResponseBody
import java.io.*
import java.nio.file.Files

fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()


fun mapCall(locationString: String): Boolean {
    var styledMap: ResponseBody? = null
    var realMap: ResponseBody? = null
    var tasks = listOf<Deferred<Dispatchers>>()
    runBlocking {
        withContext(coroutineContext) {
            async(Dispatchers.IO) {
                styledMap = MapsService.getMap(locationString, 1)
                if (styledMap != null) {
                    writeResponseBodyToDisk(styledMap!!, "analysisMap")
                }
            }
            async(Dispatchers.IO) {
                realMap = MapsService.getMap(locationString, 0)
                if (realMap != null) {
                    writeResponseBodyToDisk(realMap!!, "displayMap")
                }
            }
        }
    }

    return if (styledMap != null && realMap != null)
        true
    else
    {
        print("Error en las llamadas a la API de Maps")
        false
    }
}

fun writeResponseBodyToDisk(body: ResponseBody, fileName: String): Boolean {
    return try {
        val file = File("temp${File.separator}$fileName.png")
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileReader = ByteArray(4096)
            val fileSize = body.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)
            while (true) {
                val read: Int = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
            }
            outputStream.flush()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}