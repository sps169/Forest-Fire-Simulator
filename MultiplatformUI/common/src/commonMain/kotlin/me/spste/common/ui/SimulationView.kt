package me.spste.common.ui

import BURNT
import BURNT_COLOR
import FIRE
import FIRE_COLOR
import ForestFire
import ROAD
import ROAD_COLOR
import TREE
import TREE_COLOR
import WATER
import WATER_COLOR
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimulationView(
    imageBitmap: ImageBitmap,
    modifier: Modifier,
    simulation: ForestFire,
    animationIndex: Int,
    simulationWindowSize: IntSize,
    hasClick: Boolean
) {
    Surface(
        modifier = modifier,
        onClick = {

        }) {
        var size by remember { mutableStateOf(simulationWindowSize) }
        Image(
            bitmap = imageBitmap,
            contentDescription = "Map",
            modifier = Modifier.aspectRatio(1f, true).fillMaxSize().onSizeChanged { size = it }
        )
        Fire(imageBitmap, simulation, animationIndex, size)
        if (!hasClick) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Select fire origins")
            }
        }
    }
}

@Composable
fun Fire(imageBitmap: ImageBitmap, simulation: ForestFire, variationIndex: Int, size: IntSize) {
    val ratioHeight = size.height.toFloat() / imageBitmap.height.toFloat()
    val ratioWidth = size.width.toFloat() / imageBitmap.width.toFloat()
    Canvas(
        Modifier.aspectRatio(1f, true)
//            .requiredSize((imageBitmap.width * ratioWidth).dp, (imageBitmap.height * ratioHeight).dp)
            .border(border = BorderStroke(2.dp, Color.Black))
    ) {
        drawLine(
            FIRE_COLOR,
            Offset(
                simulation.mPosInput.toFloat() * ratioHeight,
                simulation.nPosInput.toFloat() * ratioWidth
            ),
            Offset(
                (simulation.mPosInput.toFloat() + 1) * ratioHeight,
                (simulation.nPosInput.toFloat() + 1) * ratioWidth
            )
        )
        if (simulation.result != null) {
            for (i in 0 until variationIndex) {
                simulation.result?.variation?.get(i)?.pixelUpdateList?.forEach {
                    val color = when (it.value) {
                        FIRE -> FIRE_COLOR
                        BURNT -> BURNT_COLOR
                        TREE -> TREE_COLOR
                        WATER -> WATER_COLOR
                        ROAD -> ROAD_COLOR
                        else -> TREE_COLOR
                    }
                    drawLine(
                        color,
                        Offset(
                            it.x.toFloat() * ratioHeight,
                            it.y.toFloat() * ratioWidth
                        ),
                        Offset(
                            (it.x.toFloat() + 1) * ratioHeight,
                            (it.y.toFloat() + 1) * ratioWidth
                        )
                    )
                }
            }
        }
    }
}