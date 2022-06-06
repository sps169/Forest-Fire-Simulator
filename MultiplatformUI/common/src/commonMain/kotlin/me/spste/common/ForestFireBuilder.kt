package me.spste.common

import ForestFire
import SOUTHEASTDEGREES
import me.spste.common.model.Climate
import me.spste.common.model.Wind

class ForestFireBuilder {
    var map: List<List<Int>>? = null
    var wind: Wind?= Wind(speed = 80f, direction = SOUTHEASTDEGREES)
    var climate: Climate? = Climate(temperature = 30f, humidity = 30f, precipitation = 0f, pressure = 933f)
    var mPosInput : Int = -1
    var nPosInput : Int = -1

    fun build(): ForestFire {
        var missingArgs = "Forest Fire can't be build, params missing:\n"
        var hasMissingArgs = false
        if (map == null){
            missingArgs += "- map\n"
            hasMissingArgs = true
        }
        if (wind == null) {
            missingArgs += "- wind\n"
            hasMissingArgs = true
        }

        if (climate == null) {
            missingArgs += "- climate\n"
            hasMissingArgs = true
        }
        if (mPosInput == -1) {
            missingArgs += "- mPosInput\n"
            hasMissingArgs = true
        }
        if (nPosInput == -1) {
            missingArgs += "- nPosInput\n"
            hasMissingArgs = true
        }
        if (hasMissingArgs)
            throw IllegalStateException(missingArgs)
        else
            return ForestFire(map!!, wind!!, climate!!, mPosInput, nPosInput)
    }

    fun withMap(map: List<List<Int>>) : ForestFireBuilder {
        this.map = map
        return this
    }

    fun withWind(wind: Wind) : ForestFireBuilder {
        this.wind = wind
        return this
    }

    fun withClimate(climate: Climate) : ForestFireBuilder {
        this.climate = climate
        return this
    }

    fun withPosInput(mPosInput: Int, nPosInput: Int) : ForestFireBuilder {
        this.mPosInput = mPosInput
        this.nPosInput = nPosInput
        return this
    }

}