package me.spste.android

import ANALYSIS_FILE
import DISPLAY_FILE
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
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

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var text by remember { mutableStateOf("Hello world!") }
                var displayImage by remember { mutableStateOf(ImageBitmap(1, 1)) }
                var model = FFModel(null, AndroidPropertyHandler)
                var listOfImages: List<ImageBitmap?>
                Column() {
                    Button(
                        onClick = {
                            CoroutineScope(IO).launch() {
                                val imageLoader = ImageLoader()
                                listOfImages = imageLoader.callImages("37,-2")
                                withContext(Main) {
                                    listOfImages[ANALYSIS_FILE]?.let { model.withAnalysisImage(it)}
                                    listOfImages[DISPLAY_FILE]?.let { displayImage = it }
                                }
                            }
                        }
                    ) {
                        Text("Load maps")
                    }
                    App(model, displayImage)
                }

            }
        }
    }

    @Composable
    fun App(initialModel: FFModel, displayImage: ImageBitmap) {
        Column(modifier = Modifier.wrapContentSize(Alignment.TopCenter)) {
            val defaultSize = IntSize(
                AndroidPropertyHandler.getLocalProperty("default_x_size") as Int,
                AndroidPropertyHandler.getLocalProperty("default_y_size") as Int
            )
            var click by remember {mutableStateOf(Offset(-1f, -1f))}
            var printableText by remember {mutableStateOf("")}
            var model by remember {mutableStateOf(initialModel)}
            var simulationWindowSize by remember { mutableStateOf(defaultSize) }
            val animationIndex by remember { mutableStateOf(model.animationIndex) }
            val coroutineScope = rememberCoroutineScope()
            SimulationView(
                displayImage,
                Modifier
                    .onSizeChanged { simulationWindowSize = it }
                    .pointerInput(PointerType.Touch) {
                        forEachGesture {
                            awaitPointerEventScope {
                                val pointerEventPosition = awaitPointerEvent().component1()[0].position
                                model.setInitialPixel(
                                    (pointerEventPosition.x * defaultSize.height / simulationWindowSize.height).toInt(),
                                    (pointerEventPosition.y * defaultSize.width / simulationWindowSize.width).toInt()
                                )
                                click = Offset(model.getInitialClick().get(0).toFloat(), model.getInitialClick().get(1).toFloat())
                                try {
                                    coroutineScope.launch {
                                        withContext(Main) {
                                            model.resetAnimationIndex()
                                            model.run?.resetRun()
                                            model.run()
                                            model.playAnimation()
                                        }
                                    }
                                }catch(e: Exception) {

                                }
                            }
                        }
                    }
                    .aspectRatio(1f, true).fillMaxSize(),
                model.run,
                animationIndex.value.toInt(),
                simulationWindowSize,
                click
            )
            PlayButton(
                onClick = {
//                    if (!animationIndex.isRunning || animationIndex.value > 0) {
//                        coroutineScope.launch {
//                            withContext(Main) {
//                                model.resetAnimationIndex()
//                                try {
//                                    printableText = model.run?.map.toString()
//                                    model.run()
//                                } catch (e: IllegalStateException) {
//                                    printableText = e.localizedMessage
//                                }
//                                model.run?.result?.let {
//                                    withContext(Main) {
//                                        model.playAnimation()
//                                    }
//                                }
//                            }
//                        }
//                    }
                },
                modifier = Modifier.fillMaxWidth(fraction = 2 / 3f)
            )
            Text(printableText)
        }
    }
}