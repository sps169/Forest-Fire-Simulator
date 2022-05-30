import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*

import me.spste.common.App
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Login
import me.spste.common.model.Wind
import me.spste.common.ui.*

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            Row (modifier = Modifier.fillMaxSize().padding(10.dp), ){
                SimulationView(mutableListOf(mutableListOf(1)), Modifier.border(border = BorderStroke(2.dp, Color.Black)).padding(10.dp).fillMaxWidth(fraction = 3f/4f).fillMaxHeight())
                Column (
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp).wrapContentHeight()
                ) {
                    UserLoginView(
                        Login("Teco", "pass", "spsteco11@gmail.com"),
                        Modifier.fillMaxWidth().fillMaxHeight(1/6f).padding(10.dp)
                    )
                    ParametersView(
                        Wind(36f, EASTDEGREES), Climate(30f, 30f, 30f, 30f),
                        Location(0.0, 0.0),
                        Modifier.fillMaxHeight(1/3f)
                    )
                    PlayButton(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(1/2f),
                        onClick = {

                        }
                    )
                    CustomParametersButton(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        onClick = {

                        }
                    )
                }
            }
        }
    }
}