package me.spste.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap


@Composable
fun SimulationView(imageBitmap: ImageBitmap, map: MutableList<MutableList<Int>>, modifier: Modifier) {

    Surface(modifier = modifier) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Map",
            modifier = Modifier.fillMaxWidth()
        )
        Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Text("Select fire origins")
        }
    }
}