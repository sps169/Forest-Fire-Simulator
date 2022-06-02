import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.forestfiresimulatortfg.mapsStaticAPI.MapsService
import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Login
import me.spste.common.model.Wind
import me.spste.common.ui.*
import okhttp3.ResponseBody
import java.io.*


fun main() = application {
    if (mapCall()) {
        Window(onCloseRequest = ::exitApplication) {
            MaterialTheme {
                Row(modifier = Modifier.fillMaxSize().padding(10.dp)){
                    SimulationView(loadImageBitmap(File("temp/map.png").inputStream()),mutableListOf(mutableListOf(1)), Modifier.border(border = BorderStroke(2.dp, Color.Black)).padding(10.dp).fillMaxWidth(fraction = 3f/4f).fillMaxHeight())
                    Column (
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp).wrapContentHeight()
                    ) {
                        UserLoginView(
                            Login("Teco", "pass", "spsteco11@gmail.com"),
                            Modifier.fillMaxWidth().fillMaxHeight(1/6f).padding(10.dp)
                        )
                        ParametersView(
                            Wind(36f, EASTDEGREES), Climate(30f, 30f, 30f, 30f),
                            Location(0.0, 0.0),
                            Modifier.fillMaxHeight(1/3f)
                        )
                        PlayButton(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(1/2f),
                            onClick = {

                            }
                        )
                        CustomParametersButton(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                            onClick = {

                            }
                        )
                    }
                }
            }
        }
    }
}


fun mapCall(): Boolean {
    val image = MapsService.getMap("Madrid,Spain")
    if (image != null) {
        return writeResponseBodyToDisk(image)
    }else {
        println("Error")
        return false
    }
}

private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
    return try {
        val file = File("temp/map.png")
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