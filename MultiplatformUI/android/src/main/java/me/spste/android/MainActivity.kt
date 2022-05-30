package me.spste.android

import EASTDEGREES
import me.spste.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import me.spste.common.model.Climate
import me.spste.common.model.Location
import me.spste.common.model.Wind

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App(mutableListOf(mutableListOf(1)), Climate(30f, 30f, 30f, 30f), Wind(36f, EASTDEGREES), Location(0.0, 0.0))
            }
        }
    }
}