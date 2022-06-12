package me.spste.android

import ANALYSIS_FILE
import DISPLAY_FILE
import EASTDEGREES
import NORTHDEGREES
import NORTHEASTDEGREES
import NORTHWESTDEGREES
import SOUTHDEGREES
import SOUTHEASTDEGREES
import SOUTHWESTDEGREES
import WESTDEGREES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import me.spste.android.androidUtils.AndroidPropertyHandler
import me.spste.common.FFModel
import me.spste.common.ui.PlayButton
import me.spste.common.ui.SimulationView
import me.spste.android.androidUtils.AndroidImageLoader
import me.spste.common.model.Climate
import me.spste.common.model.Wind
import me.spste.common.ui.ParametersView
import me.spste.common.ui.ReducedParametersView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var displayImage by remember { mutableStateOf(ImageBitmap(1, 1)) }
                val model by remember { mutableStateOf(FFModel(null, AndroidPropertyHandler)) }
                var locationString by remember { mutableStateOf("37,-2") }
                var listOfImages: List<ImageBitmap?>
                val focusManager = LocalFocusManager.current
                val mContext = LocalContext.current
                Column(Modifier.wrapContentHeight(Alignment.CenterVertically).padding(start = 5.dp, end = 5.dp)) {
                    Row() {
                        TextField(
                            locationString, onValueChange = {
                                locationString = it.replace(Regex("lbl\\s+([^\\s]+)"), "+")
                            }, singleLine = true
                        )
                        Button(onClick = {
                            if (model.cleanModel {
                                    Toast.makeText(mContext, "Hay una ejecución en curso", Toast.LENGTH_SHORT)
                                }) {
                                model.run?.resetRun()
                                model.resetAnimationIndex()
                                focusManager.clearFocus()
                                CoroutineScope(IO).launch() {
                                    listOfImages = AndroidImageLoader.callMapImages(locationString)
                                    withContext(Main) {
                                        listOfImages[ANALYSIS_FILE]?.let { model.withAnalysisImage(it) }
                                        listOfImages[DISPLAY_FILE]?.let { displayImage = it }
                                    }
                                }
                            }
                        }, modifier = Modifier.fillMaxWidth().fillMaxHeight(1 / 14f)) {
                            Text("Search Map")
                        }
                    }
                    App(model, displayImage)
                }

            }
        }
    }

    @Composable
    fun App(model: FFModel, displayImage: ImageBitmap) {
            val defaultSize = IntSize(
                AndroidPropertyHandler.getLocalProperty("default_x_size") as Int,
                AndroidPropertyHandler.getLocalProperty("default_y_size") as Int
            )
            var simulationWindowSize by remember { mutableStateOf(defaultSize) }
            val coroutineScope = rememberCoroutineScope()
            var click by remember { mutableStateOf(model.getInitialClick()) }
            val mContext = LocalContext.current
            SimulationView(displayImage,
                Modifier.onSizeChanged { simulationWindowSize = it }.pointerInput(PointerType.Touch) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val pointerEventPosition = awaitPointerEvent().component1()[0].position

                            if (!model.animationIndex.isRunning && model.needsClick()) {
                                model.setInitialPixel(
                                    (pointerEventPosition.x * defaultSize.height / simulationWindowSize.height).toInt(),
                                    (pointerEventPosition.y * defaultSize.width / simulationWindowSize.width).toInt()
                                )
                                click = model.getInitialClick()
                                model.cleanModel {
                                    Toast.makeText(mContext, "Hay una ejecución en curso", Toast.LENGTH_SHORT)
                                }
                            }
                        }
                    }
                }.fillMaxHeight(1/1.75f).aspectRatio(1f, true),
                model.run,
                model.animationIndex.value.toInt(),
                simulationWindowSize,
                click
            )
            ReducedParametersView(
                !model.animationIndex.isRunning,
                model.builder,
                Modifier.wrapContentHeight().fillMaxWidth(),
                AndroidImageLoader,
                onWindValueChange = {
                    if (model.builder.wind == null) {
                        model.builder.withWind(
                            Wind(
                                speed = it,
                                direction = NORTHDEGREES
                            )
                        )
                    } else {
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
                    } else {
                        model.builder.withWind(
                            Wind(
                                speed = model.builder.wind!!.speed,
                                direction = direction
                            )
                        )
                    }
                }
            )
            PlayButton(onClick = {
                if(!model.animationIndex.isRunning) {
                    coroutineScope.launch {
                        model.resetAnimationIndex()
                        try {
                            model.run()
                        } catch (e: IllegalStateException) {
                            Toast.makeText(mContext, "Error ejecutando", Toast.LENGTH_SHORT)
                        }
                    }.invokeOnCompletion {
                        coroutineScope.launch {
                            model.run?.result?.let {
                                withContext(Main) {
                                    model.playAnimation()
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(9/10f))
    }
}