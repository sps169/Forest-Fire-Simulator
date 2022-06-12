package me.spste.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.spste.common.ForestFireBuilder

import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Wind
import me.spste.common.utils.ImageLoader

@Composable
fun ParametersView(
    active: Boolean,
    builder: ForestFireBuilder,
    modifier: Modifier,
    loader: ImageLoader,
    onTemperatureChange: (Float) -> Unit,
    onWindValueChange: (Float) -> Unit,
    onWindDirectionChange: (Float) -> Unit,
    onRainfallChange: (Float) -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
        TemperatureParameterView(builder.climate?.temperature, loader, onTemperatureChange, active)
        WindParameterView(builder.wind, loader, onWindValueChange, onWindDirectionChange, active)
        RainfallParameterView(builder.climate?.precipitation, loader, onRainfallChange, active)
    }
}

@Composable
fun ReducedParametersView(
    active: Boolean,
    builder: ForestFireBuilder,
    modifier: Modifier,
    loader: ImageLoader,
    onWindValueChange: (Float) -> Unit,
    onWindDirectionChange: (Float) -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "Parameters", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))

        WindParameterView(builder.wind, loader, onWindValueChange, onWindDirectionChange, active)

    }
}

@Composable
fun DefaultParameterView(
    value: Float?,
    minValue: Float,
    maxValue: Float,
    steps: Int = 0,
    paramDesc: String,
    image: ImageBitmap?,
    onChange: (Float) -> Unit,
    editable: Boolean
) {
    var visibleValue by remember { mutableStateOf(0f) }
    if (value != null)
        visibleValue = value
    Row() {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = paramDesc,
                modifier = Modifier.size(25.dp).aspectRatio(1f).padding(end = 5.dp)
            )
        }
        Column() {
            Row {
                Text(paramDesc)
                Text(value.toString())
            }
            if (value == null) {
                Slider(visibleValue, {}, valueRange = minValue..maxValue, steps = steps, enabled = false)
            } else {
                Slider(visibleValue, onChange, valueRange = minValue..maxValue, enabled = editable)
            }
        }
    }

}

@Composable
fun RainfallParameterView(precipitation: Float?, loader: ImageLoader, onChange: (Float) -> Unit, editable: Boolean) {
    DefaultParameterView(
        precipitation, 0f, 100f, 0, "Rain", loader.loadImage("location_icon.png"), onChange, editable
    )
}

@Composable
fun TemperatureParameterView(temperature: Float?, loader: ImageLoader, onChange: (Float) -> Unit, editable: Boolean) {

    DefaultParameterView(
        temperature, -100f, 100f, 0, "Temperature", loader.loadImage("location_icon.png"), onChange, editable
    )
}

@Composable
fun WindParameterView(
    wind: Wind?,
    loader: ImageLoader,
    onValueChange: (Float) -> Unit,
    onDirectionChange: (Float) -> Unit,
    editable: Boolean
) {
    val image = loader.loadImage("location_icon.png")
    DefaultParameterView(
        wind?.speed, 0f, 150f, 0, "Wind Force", image , onValueChange, editable
    )

    Row() {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = "Wind Description",
                modifier = Modifier.size(25.dp).aspectRatio(1f).padding(end = 5.dp)
            )
        }
        Column() {
            Row {
                Text("Wind Direction: ")
                Text(when (wind?.direction) {
                    0f, 360f -> "North"
                    45f -> "North East"
                    90f -> "East"
                    135f -> "South East"
                    180f -> "South"
                    225f -> "South West"
                    270f -> "West"
                    315f -> "North West"
                    else -> {"Empty"}
                })
            }
            if (wind?.direction == null) {
                Slider(0f, onDirectionChange, valueRange = 0f..360f, steps = 7, enabled = false)
            } else {
                Slider(wind.direction, onDirectionChange, valueRange = 0f..360f, steps = 7, enabled = editable)
            }
        }
    }
}























