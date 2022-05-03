package model

import EASTDEGREES
import NORTHDEGREES
import NORTHEASTDEGREES
import NORTHWESTDEGREES
import SOUTHDEGREES
import SOUTHEASTDEGREES
import SOUTHWESTDEGREES
import WESTDEGREES
import kotlin.math.abs

data class Wind(var speed: Float, var direction: Float) {
    var n: Float = if (direction - NORTHDEGREES > 180) speed * direction else speed * direction - 180
    var s: Float = if (direction - 180 > 180) speed * direction else speed * direction - 180
    var w: Float = if (direction - 270 > 180) speed * direction else speed * direction - 180
    var e: Float = if (direction - 0 > 180) speed * direction else speed * direction - 180
    var nw: Float = if (direction - 0 > 180) speed * direction else speed * direction - 180
    var ne: Float = if (direction - 0 > 180) speed * direction else speed * direction - 180
    var sw: Float = if (direction - 0 > 180) speed * direction else speed * direction - 180
    var se: Float = if (direction - 0 > 180) speed * direction else speed * direction - 180

    fun getWindForceForDirection(speed: Float, originalDirection: Float, wantedDirection: WindDirection) {
        var wantedDirectionValue: Float = 0f
        when (wantedDirection) {
            WindDirection.NORTH-> {
                wantedDirectionValue = NORTHDEGREES.toFloat()
            }
            WindDirection.NORTHEAST-> {
                wantedDirectionValue = NORTHEASTDEGREES.toFloat()
            }
            WindDirection.EAST-> {
                wantedDirectionValue = EASTDEGREES.toFloat()
            }
            WindDirection.SOUTHEAST-> {
                wantedDirectionValue = SOUTHEASTDEGREES.toFloat()
            }
            WindDirection.SOUTH-> {
                wantedDirectionValue = SOUTHDEGREES.toFloat()
            }
            WindDirection.SOUTHWEST-> {
                wantedDirectionValue = SOUTHWESTDEGREES.toFloat()
            }
            WindDirection.WEST-> {
                wantedDirectionValue = WESTDEGREES.toFloat()
            }
            WindDirection.NORTHWEST-> {
                wantedDirectionValue = NORTHWESTDEGREES.toFloat()
            }
        }
    }

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



