import androidx.compose.runtime.Composable
import model.Climate
import model.Wind
import ui.ui
import java.io.FileNotFoundException
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt


class ForestFire(mapFile: String) {

    val map = readMap("/maps/$mapFile")
    @Composable
    fun run(): Unit {

        val windForce = 36f
        val windDirection = SOUTHDEGREES
        val temperature = 30f
        val humidity = 30f
        val precipitation = 0f
        val pressure = 933f
        val wind = Wind(windForce, windDirection)
        val climate = Climate(temperature, humidity, precipitation, pressure)

        var randomMPos = nextInt(map.size)
        var randomNPos = nextInt(map[0].size)
        while (!isBurnable(map, randomMPos, randomNPos)) {
            randomMPos = nextInt(map.size)
            randomNPos = nextInt(map[0].size)
        }
        println(
            """
            Run variables:
                - Initial click:    ($randomMPos, $randomNPos)
                - North wind:       ${wind.n}
                - South wind:       ${wind.s}
                - East wind:        ${wind.e}
                - West wind:        ${wind.w}
                - North East wind:  ${wind.ne}
                - North West wind:  ${wind.nw}
                - South East wind:  ${wind.se}
                - South West wind:  ${wind.sw}
                - Temperature:      $temperature
                - Humidity:         $humidity
                - Precipitation:    $precipitation
                - Pressure:         $pressure
                - Radiation:        to do
        """.trimIndent()
        )
        simulate_map(map, wind, climate, randomMPos, randomNPos)
    }

    @Composable
    private fun simulate_map(map: MutableList<MutableList<Int>>, wind: Wind, climate: Climate, mPos: Int, nPos: Int) {
        printMap(map)
        println()
        Thread.sleep(350)

        if (isInMap(map, mPos, nPos)) {
            map[mPos][nPos] = FIRE
        }

        ui(map){

            Thread.sleep(350)

            while (burnMap(map, wind, climate) != 0) {
//                ui(map) {}
            printMap(map)
            println()
                Thread.sleep(350)
            }
        }
        printMap(map)
        println()

    }

    private fun burnMap(map: MutableList<MutableList<Int>>, wind: Wind, climate: Climate): Int {
        var tilesBurnt = 0
        val burnQueue = mutableListOf<Array<Int>>()
        for (i in (0 until map.size)) {
            for (j in (0 until map[i].size)) {
                if (map[i][j] == FIRE)
                    burnQueue.add(arrayOf(i, j))
            }
        }

        for (tile in burnQueue) {
            tilesBurnt += expandFire(map, wind, climate, tile[0], tile[1])
        }
        return tilesBurnt
    }

    private fun expandFire(map: MutableList<MutableList<Int>>, wind: Wind, climate: Climate, i: Int, j: Int): Int {
        var burnt = 0
        for (y in (-1..1)) {
            for (x in (-1..1)) {
                if (isBurnable(map, i + y, j + x)) {
                    if (burnTile(map, wind, climate, i + y, j + x, coordinatesToWindDirection(y, x)))
                        burnt++
                }
            }
        }
        map[i][j] = BURNT
        return burnt
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
            println("Tile at ($mPos, $nPos) burnt with $chances chances\n")
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
        println("""
            Wind direction:         $windDirection
            Wind force:             $windForce
            Wind burn chances:      $windChances
            Climate burn chances:   to do
            
        """.trimIndent())
        return windChances
    }

    private fun isBurnable(map: MutableList<MutableList<Int>>, i: Int, j: Int): Boolean {
        return if (isInMap(map, i, j))
            map[i][j] == TREE
        else
            false
    }

    private fun isInMap(map: MutableList<MutableList<Int>>, i: Int, j: Int): Boolean {
        return i < map.size && j < map[0].size && i >= 0 && j >= 0
    }

    private fun readMap(path: String): MutableList<MutableList<Int>> {
        val map = mutableListOf<MutableList<Int>>()
        val lines = getResourceAsText(path)?.split("\n")
        if (lines != null) {
            for (line in lines) {
                val intRow = mutableListOf<Int>()
                for (x in line) {
                    if (x == '0')
                        intRow.add(TREE)
                    if (x == '#')
                        intRow.add(WATER)
                    if (x == '1')
                        intRow.add(FIRE)
                }
                map.add(intRow)
            }
            return map
        } else {
            throw FileNotFoundException("basemap.txt")
        }
    }

    private fun printMap(map: List<List<Int>>): Unit {
        for (line in map) {
            for (n in line) {
                if (n == FIRE)
                    print("🟥")
                if (n == TREE)
                    print("🟩")
                if (n == WATER)
                    print("🟦")
                if (n == BURNT)
                    print("🟫")
            }
            println()
        }
    }
}