package me.spste.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Wind

@Composable
fun ParametersView(wind: Wind, climate: Climate, location: Location, modifier: Modifier) {
    Column (modifier = modifier){
        Text(text = "Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        LocationParameterView(location)
        TemperatureParameterView(climate.temperature)
        WindParameterView(wind)
        RainfallParameterView(climate.precipitation)
    }
}

@Composable
fun DefaultParameterView(text: String, paramDesc: String) {
    Row () {
//        Icon(
//            imageVector = icon,
//            contentDescription = paramDesc,
//            tint = Color.Red,
//            modifier = Modifier.size(25.dp).align(Alignment.CenterVertically)
//        )
//        Image(
//            painter = painterResource(id = R.drawable.add),
//            contentDescription = paramDesc,
//            modifier = Modifier.size(25.dp).align(Alignment.CenterVertically)
//        )
        Text(text)
    }
}

@Composable
fun RainfallParameterView(precipitation: Float) {
    DefaultParameterView("${precipitation.toString()} mm/m²", "Precipitation")
}

@Composable
fun TemperatureParameterView(temperature: Float) {
    DefaultParameterView("${temperature.toString()} °C", "Temperature")

}

@Composable
fun LocationParameterView(location: Location) {
    DefaultParameterView("${location.altitude}, ${location.latitude}", "Location")

}

@Composable
fun WindParameterView(wind: Wind) {
    DefaultParameterView("${wind.speed} m/s ${wind.direction}", "Wind")
}