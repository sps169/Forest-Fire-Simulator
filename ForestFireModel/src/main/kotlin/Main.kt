import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


fun main(args: Array<String>) = application {
    val model = ForestFire("basemap.txt")
    Window(
        onCloseRequest = ::exitApplication,
        title = "Forest Fire Simulator"
    ) {
        println("Welcome to the FFM! 🔥")
        println("Program arguments: ${args.joinToString()}")

        model.run()

    }
}