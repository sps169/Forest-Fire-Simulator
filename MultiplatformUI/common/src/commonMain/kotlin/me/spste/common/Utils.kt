
import kotlinx.coroutines.*
import me.spste.common.mapsStaticAPI.MapsService
import me.spste.common.utils.PropertiesHandler
import okhttp3.ResponseBody
import java.io.*

fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()


fun mapCall(locationString: String, propertiesHandler: PropertiesHandler): Array<File?> {
    var styledMap: ResponseBody? = null
    var realMap: ResponseBody? = null
    var tasks = listOf<Deferred<Dispatchers>>()
    var filesArray = arrayOf<File?>()
    runBlocking {
        withContext(coroutineContext) {
            async(Dispatchers.IO) {
                styledMap = MapsService(propertiesHandler).getMap(locationString, 1)
                if (styledMap != null) {
                    filesArray[0] = writeResponseBodyToDisk(styledMap!!, "analysisMap.png")
                }
            }
            async(Dispatchers.IO) {
                realMap = MapsService(propertiesHandler).getMap(locationString, 0)
                if (realMap != null) {
                    filesArray[1] = writeResponseBodyToDisk(realMap!!, "displayMap.png")
                }
            }
        }
    }
    return filesArray
}

fun writeResponseBodyToDisk(body: ResponseBody, fileName: String): File? {
    return try {
        val dir = File("simulationMaps")
        if (!dir.isDirectory)
            dir.mkdir()
        val file = File("${dir.path}${File.separator}$fileName")
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileReader = ByteArray(4096)
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
            file
        } catch (e: IOException) {
            e.printStackTrace()
            file
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}