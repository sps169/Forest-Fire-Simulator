package me.spste.common.model

import EASTDEGREES
import NORTHDEGREES
import NORTHEASTDEGREES
import NORTHWESTDEGREES
import SOUTHDEGREES
import SOUTHEASTDEGREES
import SOUTHWESTDEGREES
import WESTDEGREES
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos

data class Wind(
    var n: Float = 0f,
    var s: Float = 0f,
    var w: Float = 0f,
    var e: Float = 0f,
    var nw: Float = 0f,
    var ne: Float = 0f,
    var sw: Float = 0f,
    var se: Float = 0f,
    var speed: Float,
    var direction: Float
)  {
    constructor(speed: Float, direction: Float) : this (
        n = calculateWindForceForDirection(speed, direction, WindDirection.NORTH),
        s = calculateWindForceForDirection(speed, direction, WindDirection.SOUTH),
        w = calculateWindForceForDirection(speed, direction, WindDirection.WEST),
        e = calculateWindForceForDirection(speed, direction, WindDirection.EAST),
        nw = calculateWindForceForDirection(speed, direction, WindDirection.NORTHWEST),
        ne = calculateWindForceForDirection(speed, direction, WindDirection.NORTHEAST),
        sw = calculateWindForceForDirection(speed, direction, WindDirection.SOUTHWEST),
        se = calculateWindForceForDirection(speed, direction, WindDirection.SOUTHEAST),
        speed = speed,
        direction = direction
    )

    enum class WindDirection {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NORTHEAST,
        NORTHWEST,
        SOUTHEAST,
        SOUTHWEST
    }
}

fun calculateWindForceForDirection(speed: Float, originalDirection: Float, wantedDirection: Wind.WindDirection) : Float {
    var wantedDirectionValue: Float = 0f
    when (wantedDirection) {
        Wind.WindDirection.NORTH-> {
            wantedDirectionValue = NORTHDEGREES.toFloat()
        }
        Wind.WindDirection.NORTHEAST-> {
            wantedDirectionValue = NORTHEASTDEGREES.toFloat()
        }
        Wind.WindDirection.EAST-> {
            wantedDirectionValue = EASTDEGREES.toFloat()
        }
        Wind.WindDirection.SOUTHEAST-> {
            wantedDirectionValue = SOUTHEASTDEGREES.toFloat()
        }
        Wind.WindDirection.SOUTH-> {
            wantedDirectionValue = SOUTHDEGREES.toFloat()
        }
        Wind.WindDirection.SOUTHWEST-> {
            wantedDirectionValue = SOUTHWESTDEGREES.toFloat()
        }
        Wind.WindDirection.WEST-> {
            wantedDirectionValue = WESTDEGREES.toFloat()
        }
        Wind.WindDirection.NORTHWEST-> {
            wantedDirectionValue = NORTHWESTDEGREES.toFloat()
        }
    }

    return speed * cos(PI /180 *(abs(wantedDirectionValue - originalDirection))).toFloat()
}


