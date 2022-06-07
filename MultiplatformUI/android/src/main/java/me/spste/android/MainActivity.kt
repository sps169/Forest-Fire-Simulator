package me.spste.android

import ANALYSIS_FILE
import DISPLAY_FILE
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.tween
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
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import me.spste.android.androidUtils.AndroidPropertyHandler
import me.spste.android.androidUtils.ImageLoader
import me.spste.common.FFModel
import me.spste.common.ui.PlayButton
import me.spste.common.ui.SimulationView

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
                Column() {
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
                                    val imageLoader = ImageLoader()
                                    listOfImages = imageLoader.callImages(locationString)
                                    withContext(Main) {
                                        listOfImages[ANALYSIS_FILE]?.let { model.withAnalysisImage(it) }
                                        listOfImages[DISPLAY_FILE]?.let { displayImage = it }
                                    }
                                }
                            }
                        }) {
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
        Column(modifier = Modifier.wrapContentSize(Alignment.TopCenter)) {
            val defaultSize = IntSize(
                AndroidPropertyHandler.getLocalProperty("default_x_size") as Int,
                AndroidPropertyHandler.getLocalProperty("default_y_size") as Int
            )
            var simulationWindowSize by remember { mutableStateOf(defaultSize) }
            val coroutineScope = rememberCoroutineScope()
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
                                model.cleanModel {
                                    Toast.makeText(mContext, "Hay una ejecución en curso", Toast.LENGTH_SHORT)
                                }
                            }

                        }
                    }
                }.aspectRatio(1f, true).fillMaxSize(),
                model.run,
                model.animationIndex.value.toInt(),
                simulationWindowSize,
                model.getInitialClick()
            )
            PlayButton(
                onClick = {
                    if (!model.animationIndex.isRunning) {
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
                }, modifier = Modifier.fillMaxWidth(fraction = 2 / 3f)
            )
        }
    }
}