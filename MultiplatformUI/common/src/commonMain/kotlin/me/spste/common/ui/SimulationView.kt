package me.spste.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun SimulationView(map: MutableList<MutableList<Int>>, modifier : Modifier) {
    Surface(modifier = modifier) {

        Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Text("Select fire origins")
        }
    }
}