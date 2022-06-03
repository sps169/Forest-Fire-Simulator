import me.spste.common.model.*
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt


class ForestFire(
    val map: MutableList<MutableList<Int>>,
    val wind: Wind = Wind(speed = 36f, direction = SOUTHDEGREES),
    val climate: Climate = Climate(temperature = 30f, humidity = 30f, precipitation = 0f, pressure = 933f)
) {

    fun run(): SimulationResult {
        var randomMPos = nextInt(map.size)
        var randomNPos = nextInt(map[0].size)
        while (!isBurnable(map, randomMPos, randomNPos)) {
            randomMPos = nextInt(map.size)
            randomNPos = nextInt(map[0].size)
        }
//        println(
//            """
//            Run variables:
//                - Initial click:    ($randomMPos, $randomNPos)
//                - North wind:       ${wind.n}
//                - South wind:       ${wind.s}
//                - East wind:        ${wind.e}
//                - West wind:        ${wind.w}
//                - North East wind:  ${wind.ne}
//                - North West wind:  ${wind.nw}
//                - South East wind:  ${wind.se}
//                - South West wind:  ${wind.sw}
//                - Temperature:      $temperature
//                - Humidity:         $humidity
//                - Precipitation:    $precipitation
//                - Pressure:         $pressure
//                - Radiation:        to do
//        """.trimIndent()
//        )
        return simulate_map(map, wind, climate, randomMPos, randomNPos)
    }

    private fun simulate_map(map: List<List<Int>>, wind: Wind, climate: Climate, mPos: Int, nPos: Int) : SimulationResult{
        val mapUpdateList = mutableMapOf<Int,MapUpdate>()
        val newMap = copyMap(map)
        var order = 0
        if (isInMap(newMap, mPos, nPos)) {
            newMap[mPos][nPos] = FIRE
            mapUpdateList[order] = MapUpdate(listOf(PixelUpdate(mPos, nPos, FIRE)))
            order+=1
        }
        var mapUpdate = burnMap(newMap, wind, climate)
        while(mapUpdate.pixelUpdateList.isNotEmpty()) {
            mapUpdateList[order] = mapUpdate
            order+=1
            mapUpdate = burnMap(newMap, wind, climate)
        }
        return SimulationResult(map, newMap, mapUpdateList, wind, climate)
    }

    private fun burnMap(map: MutableList<MutableList<Int>>, wind: Wind, climate: Climate): MapUpdate {
        val burnQueue = mutableListOf<Array<Int>>()
        val updatedPixels = mutableListOf<PixelUpdate>()
        for (i in (0 until map.size)) {
            for (j in (0 until map[i].size)) {
                if (map[i][j] == FIRE)
                    burnQueue.add(arrayOf(i, j))
            }
        }
        for (tile in burnQueue) {
            updatedPixels.addAll(expandTileFire(map, wind, climate, tile[0], tile[1]))
        }
        return MapUpdate(updatedPixels)
    }

    private fun expandTileFire(map: MutableList<MutableList<Int>>, wind: Wind, climate: Climate, i: Int, j: Int): List<PixelUpdate> {
        val listPixelUpdate = mutableListOf<PixelUpdate>()
        for (y in (-1..1)) {
            for (x in (-1..1)) {
                if (isBurnable(map, i + y, j + x)) {
                    if (burnTile(map, wind, climate, i + y, j + x, coordinatesToWindDirection(y, x)))
                        listPixelUpdate.add(PixelUpdate(i + y, j + x, FIRE))
                }
            }
        }
        map[i][j] = BURNT
        listPixelUpdate.add(PixelUpdate(i, j, BURNT))
        return listPixelUpdate
    }

    private fun coordinatesToWindDirection(y: Int, x: Int): Wind.WindDirection {
        if (x == -1) {
            if (y == -1) {
                return Wind.WindDirection.NORTHWEST
            }
            if (y == 0) {
                return Wind.WindDirection.WEST
            }
            if (y == 1) {
                return Wind.WindDirection.SOUTHWEST
            }
        }
        if (x == 0) {
            if (y == -1) {
                return Wind.WindDirection.NORTH
            }
            if (y == 0) {
                return Wind.WindDirection.NORTH
            }
            if (y == 1) {
                return Wind.WindDirection.SOUTH
            }
        }
        if (x == 1) {
            if (y == -1) {
                return Wind.WindDirection.NORTHEAST
            }
            if (y == 0) {
                return Wind.WindDirection.EAST
            }
            if (y == 1) {
                return Wind.WindDirection.SOUTHEAST
            }
        }
        return Wind.WindDirection.NORTH
    }

    private fun burnTile(
        map: MutableList<MutableList<Int>>,
        wind: Wind,
        climate: Climate,
        mPos: Int,
        nPos: Int,
        windDirection: Wind.WindDirection
    ): Boolean {
        val chances = calculateBurnChances(wind, climate, windDirection)
        return if (nextFloat() <= chances) {
            map[mPos][nPos] = FIRE
//            println("Tile at ($mPos, $nPos) burnt with $chances chances\n")
            true
        } else {
            false
        }
    }

    private fun calculateBurnChances(wind: Wind, climate: Climate, windDirection: Wind.WindDirection): Float {
        val windForce = when (windDirection) {
            Wind.WindDirection.NORTH -> wind.n
            Wind.WindDirection.SOUTH -> wind.s
            Wind.WindDirection.EAST -> wind.e
            Wind.WindDirection.WEST -> wind.w
            Wind.WindDirection.NORTHEAST -> wind.ne
            Wind.WindDirection.NORTHWEST -> wind.nw
            Wind.WindDirection.SOUTHEAST -> wind.se
            Wind.WindDirection.SOUTHWEST -> wind.sw
        }
        var windChances = 1f - (1f / (0.05f * windForce + 1))
        windChances = if (windForce > 0.1f) windChances else 0f
//        println("""
//            Wind direction:         $windDirection
//            Wind force:             $windForce
//            Wind burn chances:      $windChances
//            Climate burn chances:   to do
//
//        """.trimIndent())
        return windChances
    }

    private fun isBurnable(map: List<List<Int>>, i: Int, j: Int): Boolean {
        return if (isInMap(map, i, j))
            map[i][j] == TREE
        else
            false
    }

    private fun isInMap(map: List<List<Int>>, i: Int, j: Int): Boolean {
        return i < map.size && j < map[0].size && i >= 0 && j >= 0
    }


    private fun copyMap(
        map: List<List<Int>>
    ): MutableList<MutableList<Int>> {
        val newMap = mutableListOf<MutableList<Int>>()
        for (i in 0 until map.size) {
            newMap.add(mutableListOf())
            for (j in 0 until map[i].size) {
                newMap[i].add(map[i][j])
            }
        }
        return newMap
    }
}