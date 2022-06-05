import me.spste.common.model.*
import kotlin.random.Random.Default.nextFloat

class ForestFire(
    val map: List<List<Int>>,
    val wind: Wind = Wind(speed = 80f, direction = SOUTHEASTDEGREES),
    val climate: Climate = Climate(temperature = 30f, humidity = 30f, precipitation = 0f, pressure = 933f),
    var mPosInput : Int = -1,
    var nPosInput : Int = -1
) {
    private var newMap: MutableList<MutableList<Int>> = copyMap(map)
    var result: SimulationResult? = null
    fun run(): SimulationResult {
        val mapUpdateList = mutableMapOf<Int,MapUpdate>()
        if (!(mPosInput == -1 && nPosInput == -1)) {
            newMap = copyMap(map)
            newMap[mPosInput][nPosInput] = FIRE
            mapUpdateList[0] = MapUpdate(listOf(PixelUpdate(mPosInput, nPosInput, FIRE)))
            var order = 1
            var mapUpdate = burnMap()
            while(mapUpdate.pixelUpdateList.isNotEmpty() || order == 1) {
                mapUpdateList[order] = mapUpdate
                order+=1
                mapUpdate = burnMap()
            }
        }
        result = SimulationResult(map, newMap, mapUpdateList, wind, climate)
        return result as SimulationResult
    }

    fun resetRun() : SimulationResult? {
        result = null
        return result
    }

    fun setInitialPixel(mPos: Int, nPos: Int) {
        if (!(mPosInput == -1 && nPosInput == -1)) {
            newMap[mPosInput][nPosInput] = map[mPosInput][nPosInput]
        }
        if (isInMap(mPos, nPos) && isBurnable(mPos, nPos)) {
            mPosInput = mPos
            nPosInput = nPos
        }
    }

    private fun burnMap(): MapUpdate {
        val burnQueue = mutableListOf<Array<Int>>()
        val updatedPixels = mutableListOf<PixelUpdate>()
        for (i in (0 until newMap.size)) {
            for (j in (0 until newMap[i].size)) {
                if (newMap[i][j] == FIRE)
                    burnQueue.add(arrayOf(i, j))
            }
        }
        for (tile in burnQueue) {
            updatedPixels.addAll(expandTileFire(tile[0], tile[1]))
        }
        return MapUpdate(updatedPixels)
    }

    private fun expandTileFire(i: Int, j: Int): List<PixelUpdate> {
        val listPixelUpdate = mutableListOf<PixelUpdate>()
        for (x in (-1..1)) {
            for (y in (-1..1)) {
                if (isBurnable(i + x, j + y)) {
                    if (burnTile(i + x, j + y, coordinatesToWindDirection(x, y)))
                        if (!(x == 0 && y == 0))
                            listPixelUpdate.add(PixelUpdate(i + x, j + y, FIRE))
                }
            }
        }
        newMap[i][j] = BURNT
        listPixelUpdate.add(PixelUpdate(i, j, BURNT))
        return listPixelUpdate
    }

    private fun coordinatesToWindDirection(x: Int, y: Int): Wind.WindDirection {
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
        mPos: Int,
        nPos: Int,
        fireDirection: Wind.WindDirection
    ): Boolean {
        val chances = calculateBurnChances(fireDirection)
        return if (nextFloat() <= chances) {
            newMap[mPos][nPos] = FIRE
//            println("Tile at ($mPos, $nPos) burnt with $chances chances\n")
            true
        } else {
            false
        }
    }

    private fun calculateBurnChances(windDirection: Wind.WindDirection): Float {
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

    private fun isBurnable(i: Int, j: Int): Boolean {
        return if (isInMap(i, j))
            newMap[i][j] == TREE
        else
            false
    }

    private fun isInMap(i: Int, j: Int): Boolean {
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

    override fun toString(): String {
        return "ForestFire(map=$map, wind=$wind, climate=$climate, mPosInput=$mPosInput, nPosInput=$nPosInput, newMap=$newMap)"
    }


}