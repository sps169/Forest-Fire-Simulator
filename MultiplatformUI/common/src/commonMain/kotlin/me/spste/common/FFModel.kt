package me.spste.common

import ForestFire
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import me.spste.common.model.Climate
import me.spste.common.model.Wind
import me.spste.common.utils.PropertiesHandler
import me.spste.common.utils.generateMapFromImage

class FFModel(
    var analysisImage: ImageBitmap?,
    var propertiesHandler: PropertiesHandler
) {
    val builder = ForestFireBuilder()
    var run: ForestFire? = null
    var animationIndex = Animatable(0f)

    fun cleanModel(cantRestartWindow: () -> Unit): Boolean {
        if (!animationIndex.isRunning) {
            run?.resetRun()
            resetAnimationIndex()
            return true
        }else {
            cantRestartWindow
            return false
        }
    }

    fun run() {
        run = builder.build()
        run!!.run()
    }

    fun canRun (): Boolean {
        return builder.canRun()
    }

    fun needsClick(): Boolean {
        return builder.needsClick()
    }

    fun withWind(wind: Wind): FFModel {
        builder.withWind(wind)
        return this
    }

    fun withClimate(climate: Climate) : FFModel {
        builder.withClimate(climate)
        return this
    }

    fun withAnalysisImage(analysisImage: ImageBitmap): FFModel {
        this.analysisImage = analysisImage
        this.builder.withMap(generateMapFromImage(analysisImage, propertiesHandler))
        return this
    }

    fun withPropertiesHandler(propertiesHandler: PropertiesHandler): FFModel {
        this.propertiesHandler = propertiesHandler
        return this
    }


    fun setInitialPixel(mPosInput: Int, nPosInput: Int) {
        builder.withPosInput(mPosInput, nPosInput)
    }

    fun getInitialClick(): Offset {
        return Offset(builder.mPosInput.toFloat(), builder.nPosInput.toFloat())
    }

    fun resetAnimationIndex() {
        run?.let {
            runBlocking {
                animationIndex.snapTo(0f)
            }
        }
    }

    suspend fun playAnimation() {
        run?.result?.let {
            coroutineScope {
                animationIndex.animateTo(it.variation.size.toFloat(), animationSpec = tween((it.variation.size * 50 * 50/run!!.wind.speed).toInt()))
            }
        }
    }

    override fun toString(): String {
        return "FFModel(analysisImage=$analysisImage, propertiesHandler=$propertiesHandler, builder=$builder, run=${run.toString()}, animationIndex=$animationIndex)"
    }


}