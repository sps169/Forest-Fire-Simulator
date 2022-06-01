package me.spste.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Wind
import me.spste.common.ui.ParametersView
import me.spste.common.ui.PlayButton
import me.spste.common.ui.SimulationView

@Preview
@Composable
fun AppPreview() {



}

@Composable
fun App(image: ImageBitmap, map: MutableList<MutableList<Int>>, climate: Climate, wind: Wind, location: Location) {
    Column (modifier = Modifier.padding(10.dp)){
        SimulationView(image, map = map, Modifier.border(border = BorderStroke(2.dp, Color.Black)).padding(10.dp).fillMaxWidth().fillMaxHeight(1/2f))
        //todo switch parameters view with edit parameters view
        ParametersView(climate = climate, wind = wind, location = location, modifier = Modifier.fillMaxHeight(2/3f))
        PlayButton(modifier = Modifier.fillMaxWidth().fillMaxHeight(), onClick = {})
    }
}