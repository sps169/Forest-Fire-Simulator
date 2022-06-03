package me.spste.common.ui

import BURNT
import BURNT_COLOR
import FIRE
import FIRE_COLOR
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import me.spste.common.model.SimulationResult
import me.spste.common.utils.generateMapFromImage


@Composable
fun SimulationView(imageBitmap: ImageBitmap, modifier: Modifier, result: SimulationResult) {
    Surface(modifier = modifier) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Map",
            modifier = Modifier
        )
        Fire(imageBitmap, result)//generateMapFromImage(imageBitmap))
//        Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
//            Text("Select fire origins")
//        }
    }
}

@Composable
fun Fire(imageBitmap: ImageBitmap, result: SimulationResult) {
    println(result.result)
    Canvas(Modifier.size(result.map.size.dp, result.map[0].size.dp).border(border = BorderStroke(2.dp, Color.Black))) {
        for (i in result.map.indices) {
            for (j in 0 until result.map[i].size) {
                val color = when (result.result[i][j]) {
                    FIRE -> FIRE_COLOR
                    BURNT -> BURNT_COLOR
                    TREE -> TREE_COLOR
                    WATER -> WATER_COLOR
                    ROAD -> ROAD_COLOR
                    else -> TREE_COLOR
                }
                if (result.result[i][j] == FIRE || result.result[i][j] == BURNT) {
                    drawLine(color, Offset(i.toFloat(), j.toFloat()), Offset(i.toFloat() + 1, j.toFloat() + 1))
                }
            }
        }
//        for (i in 0 until  result.variation.size) {
//            result.variation[i]?.pixelUpdateList?.forEach {
//                val color = when (it.value) {
//                    FIRE -> FIRE_COLOR
//                    BURNT -> BURNT_COLOR
//                    TREE -> TREE_COLOR
//                    WATER -> WATER_COLOR
//                    ROAD -> ROAD_COLOR
//                    else -> TREE_COLOR
//                }
//                drawLine(color, Offset(i.toFloat(), it.x.toFloat()), Offset(i.toFloat() + 1, it.y.toFloat() + 1))
//            }
//        }
    }
}