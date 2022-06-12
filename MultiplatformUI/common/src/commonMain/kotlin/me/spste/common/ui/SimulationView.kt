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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimulationView(
    imageBitmap: ImageBitmap?,
    modifier: Modifier,
    simulation: ForestFire?,
    animationIndex: Int,
    simulationWindowSize: IntSize,
    click: Offset?
) {
    Surface(
        modifier = modifier,
        onClick = {

        }) {
        var size by remember { mutableStateOf(simulationWindowSize) }
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Map",
                modifier = Modifier.aspectRatio(1f, true).fillMaxSize().onSizeChanged { size = it }
            )
            Fire(imageBitmap, simulation, animationIndex, size, click)
        }
        if ((click == null) || (click.x == -1f) && (click.y == -1f)) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Select fire origins")
            }
        }
    }
}

@Composable
fun Fire(imageBitmap: ImageBitmap, simulation: ForestFire?, variationIndex: Int, canvasSize: IntSize, click: Offset?) {
    val ratioHeight = canvasSize.height.toFloat() / imageBitmap.height.toFloat()
    val ratioWidth = canvasSize.width.toFloat() / imageBitmap.width.toFloat()
    Canvas(
        Modifier.aspectRatio(1f, true)
    ) {

        if ((click != null) && (click.x != -1f) && (click.y != -1f)) {
            drawLine(
                FIRE_COLOR,
                Offset(
                    click.x * ratioHeight,
                    click.y * ratioWidth
                ),
                Offset(
                    (click.x + 1) * ratioHeight,
                    (click.y + 1) * ratioWidth
                ),
                5f
            )
        }
        if (simulation != null) {
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
}