package me.spste.android

import EASTDEGREES
import ForestFire
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.runBlocking
import mapCall
import me.spste.common.App
import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Wind
import me.spste.common.utils.generateMapFromImage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App(
                    loadImageBitmap = {
                        BitmapFactory.decodeFile(it).asImageBitmap()
                    }
                )
            }
        }
    }
}