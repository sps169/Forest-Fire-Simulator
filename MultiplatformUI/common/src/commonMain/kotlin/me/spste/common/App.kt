package me.spste.common

import ForestFire
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mapCall
import me.spste.common.model.Location
import me.spste.common.model.Login
import me.spste.common.ui.*
import me.spste.common.utils.generateMapFromImage
import java.io.File
import java.io.FileInputStream

@Composable
fun App(loadImageBitmap: (fileLocation: String) -> ImageBitmap) {
    var locationString = "37,-2"
    if (mapCall(locationString)) {
        val analysisImage = loadImageBitmap("temp${File.separator}analysisMap.png")
        val displayImage = loadImageBitmap("temp${File.separator}displayMap.png")
        val map = generateMapFromImage(analysisImage)
        val run = ForestFire(map)
        var result by remember { mutableStateOf(run.result) }
        val coroutineScope = rememberCoroutineScope()
        val animationScope = rememberCoroutineScope()
        var variationIndex = remember {
            Animatable(0f)
        }
        var animationSize by remember { mutableStateOf(1f) }
        var animationDuration by remember { mutableStateOf(100) }
        var hasClick by remember { mutableStateOf(false) }
        var simulationWindowSize by remember { mutableStateOf(IntSize(run.map.size, run.map[0].size)) }
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            SimulationView(
                displayImage,
                Modifier
                    .clickable(indication = null, interactionSource = MutableInteractionSource(), onClick = {})
                    .onSizeChanged { simulationWindowSize = it }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                run.setInitialPixel(
                                    (it.x * map.size / simulationWindowSize.height).toInt(),
                                    (it.y * map[0].size / simulationWindowSize.width).toInt()
                                )
                                hasClick = true
                                animationScope.launch {
                                    variationIndex.snapTo(0f)
                                }
                                result = run.resetRun()
                            }
                        )
                    }.aspectRatio(1f, true).fillMaxHeight().fillMaxWidth(2 / 3f),
                run,
                variationIndex.value.toInt(),
                simulationWindowSize,
                hasClick
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp).wrapContentHeight()
            ) {
                UserLoginView(
                    Login("Teco", "pass", "spsteco11@gmail.com"),
                    Modifier.fillMaxWidth().fillMaxHeight(1 / 6f).padding(10.dp)
                )
                ParametersView(
                    run.wind, run.climate,
                    Location(0.0, 0.0),
                    Modifier.fillMaxHeight(1 / 3f)
                )
                PlayButton(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(1 / 2f),
                    onClick = {
                        if (!variationIndex.isRunning || variationIndex.value > 0) {
                            coroutineScope.launch {
                                variationIndex.snapTo(0f)
                                result = run.run()
                            }.invokeOnCompletion {
                                animationScope.launch {
                                    if (run.result != null) {
                                        animationSize = run.result!!.variation.size.toFloat()
                                        animationDuration = run.result!!.variation.size * 100
                                    } else {
                                        animationSize = 1f
                                        animationDuration = 100
                                    }
                                    variationIndex.animateTo(animationSize, animationSpec = tween(animationDuration))
                                }
                            }
                        }
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