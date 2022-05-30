package me.spste.common

import EASTDEGREES
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Wind

@Preview
@Composable
fun AppPreview() {
    App(
        mutableListOf(mutableListOf(1)),
        Climate(30f, 30f, 30f, 30f), Wind(36f, EASTDEGREES),
        Location(0.0, 0.0)
    )
}