package ui

import BURNT
import FIRE
import ForestFire
import TREE
import WATER
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ui(map: MutableList<MutableList<Int>>, onPlayClick: () -> Unit) {
    var mutableMap by remember { mutableStateOf(map)}
    Row {
        Column {
            for (line in mutableMap) {
                Row() {
                    for (n in line) {
                        if (n == FIRE)
                            pixel(Color.Red)
                        if (n == TREE)
                            pixel(Color.Green)
                        if (n == WATER)
                            pixel(Color.Blue)
                        if (n == BURNT)
                            pixel(Color.Gray)
                    }
                }
            }
        }
        Column (){
            Button(onClick = onPlayClick) {
                Text("GO")
            }
        }
    }
}

@Composable
fun pixel(color: Color) {
    Surface (
        color = color,
        modifier = Modifier.size(7.dp, 7.dp)
        ){

    }
}
