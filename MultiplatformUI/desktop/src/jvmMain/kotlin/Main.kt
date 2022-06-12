import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spste.common.FFModel
import me.spste.common.model.Location
import me.spste.common.model.Login
import me.spste.common.model.Climate
import me.spste.common.model.Wind
import me.spste.common.ui.*
import utils.DesktopImageLoader
import utils.DesktopPropertyHandler

fun main() = application {
    var displayImage by remember { mutableStateOf(ImageBitmap(1, 1)) }
    val model by remember { mutableStateOf(FFModel(null, DesktopPropertyHandler)) }
    var locationString by remember { mutableStateOf("37,-2") }
    var listOfImages: List<ImageBitmap?>

    MaterialTheme {
        Window(onCloseRequest = ::exitApplication) {
            Column() {
                Row() {
                    TextField(
                        locationString, onValueChange = {
                            locationString = it.replace(Regex("lbl\\s+([^\\s]+)"), "+")
                        }, singleLine = true, modifier = Modifier.fillMaxWidth(3 / 4f).padding(5.dp)
                    )
                    Button(
                        onClick = {
                            if (model.cleanModel {

                                }) {
                                model.run?.resetRun()
                                model.resetAnimationIndex()
                                CoroutineScope(Dispatchers.IO).launch() {
                                    listOfImages = DesktopImageLoader.callMapImages(locationString)
//                                withContext(Dispatchers.Main) {
                                    listOfImages[ANALYSIS_FILE]?.let { model.withAnalysisImage(it) }
                                    listOfImages[DISPLAY_FILE]?.let { displayImage = it }
//                                }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Search Map")
                    }
                }
                App(model, displayImage)
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App(model: FFModel, displayImage: ImageBitmap) {
    print("app recomposed\n")
    Row(modifier = Modifier.wrapContentSize(Alignment.TopCenter)) {
        val defaultSize = IntSize(
            DesktopPropertyHandler.getLocalProperty("default_x_size", "getMap.properties").toInt(),
            DesktopPropertyHandler.getLocalProperty("default_y_size", "getMap.properties").toInt()
        )
        var simulationWindowSize by remember { mutableStateOf(defaultSize) }
        val coroutineScope = rememberCoroutineScope()
        var click by remember { mutableStateOf(model.getInitialClick()) }
        SimulationView(displayImage,
            Modifier.onSizeChanged { simulationWindowSize = it }
                .onPointerEvent(PointerEventType.Press) {
                    if (!model.animationIndex.isRunning && model.needsClick()) {
                        model.setInitialPixel(
                            (it.changes.first().position.x * defaultSize.height / simulationWindowSize.height).toInt(),
                            (it.changes.first().position.y * defaultSize.width / simulationWindowSize.width).toInt()
                        )
                        click = model.getInitialClick()
                        model.cleanModel {

                        }
                    }
                }
                .aspectRatio(1f, true).fillMaxSize(),
            model.run,
            model.animationIndex.value.toInt(),
            simulationWindowSize,
            click
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp).wrapContentHeight()
        ) {
            ParametersView(
                !model.animationIndex.isRunning,
                model.builder,
                Modifier.wrapContentHeight().fillMaxWidth(),
                DesktopImageLoader,
                onRainfallChange = {
                    if (model.builder.climate == null) {
                        model.builder.withClimate(
                            Climate(
                                temperature = 20f,
                                precipitation = it
                            )
                        )
                    }else {
                        model.builder.withClimate(
                            Climate(
                                temperature = model.builder.climate!!.temperature,
                                precipitation = it
                            )
                        )
                    }
                },
                onTemperatureChange = {
                    if (model.builder.climate == null) {
                        model.builder.withClimate(
                            Climate(
                                temperature = it,
                                precipitation = 0f
                            )
                        )
                    }else {
                        model.builder.withClimate(
                            Climate(
                                temperature = it,
                                precipitation = model.builder.climate!!.precipitation
                            )
                        )
                    }
                },
                onWindValueChange = {
                    if (model.builder.wind == null) {
                        model.builder.withWind(
                            Wind(
                                speed = it,
                                direction = NORTHDEGREES
                            )
                        )
                    }else {
                        model.builder.withWind(
                            Wind(
                                speed = it,
                                direction = model.builder.wind!!.direction
                            )
                        )
                    }
                },
                onWindDirectionChange = {
                    val direction = when (it) {
                        in 0f..45f -> NORTHDEGREES
                        in 45f..90f -> NORTHEASTDEGREES
                        in 90f..135f -> EASTDEGREES
                        in 135f..180f -> SOUTHEASTDEGREES
                        in 180f..225f -> SOUTHDEGREES
                        in 225f..270f -> SOUTHWESTDEGREES
                        in 270f..315f -> WESTDEGREES
                        in 315f..360f -> NORTHWESTDEGREES
                        else -> NORTHDEGREES
                    }
                    if (model.builder.wind == null) {
                        model.builder.withWind(
                            Wind(
                                speed = 36f,
                                direction = direction
                            )
                        )
                    }else {
                        model.builder.withWind(
                            Wind(
                                speed = model.builder.wind!!.speed,
                                direction = direction
                            )
                        )
                    }
                }
            )
            PlayButton(
                onClick = {
                    if (!model.animationIndex.isRunning) {
                        coroutineScope.launch {
                            model.resetAnimationIndex()
                            try {
                                model.run()
                            } catch (e: IllegalStateException) {

                            }
                        }.invokeOnCompletion {
                            coroutineScope.launch {
                                model.run?.result?.let {
                                    model.playAnimation()
//                                    running = true
                                }
                            }
                        }
                    }
                }, modifier = Modifier.fillMaxWidth(fraction = 4 / 5f).fillMaxHeight(1 / 2f)
            )
        }
    }
}