import model.Climate
import model.Wind
import java.io.FileNotFoundException
import kotlin.random.Random.Default.nextInt

class ForestFire {

    fun run(mPos: Int, nPos: Int) : Unit {

        val map = readMap()
        val wind = Wind()
        val climate = Climate()

        var randomMPos = nextInt(map.size)
        var randomNPos = nextInt(map[0].size)
        while(!isBurnable(map, randomMPos, randomNPos)) {
            randomMPos = nextInt(map.size)
            randomNPos = nextInt(map[0].size)
        }
        simulate_map(map, randomMPos, randomNPos)
    }

    private fun simulate_map(map: MutableList<MutableList<Int>> wind: Wind, climate: Climate, mPos: Int, nPos: Int) {
        printMap(map)
        println()
        Thread.sleep(350)

        if (isInMap(map, mPos, nPos)) {
            map[mPos][nPos] = FIRE
        }
        printMap(map)
        println()
        Thread.sleep(350)

        while (burnMap(map) != 0) {
            printMap(map)
            println()
            Thread.sleep(350)
        }
    }

    private fun burnMap(map: MutableList<MutableList<Int>>) : Int{
        var tilesBurnt = 0
        val burnQueue = mutableListOf<Array<Int>>()
        for (i in (0 until map.size)) {
            for (j in (0 until map[i].size)) {
                if (map[i][j] == FIRE)
                    burnQueue.add(arrayOf(i, j))
            }
        }

        for (tile in burnQueue) {
            tilesBurnt += expandFire(map, tile[0], tile[1])
        }
        return tilesBurnt
    }

    private fun expandFire(map: MutableList<MutableList<Int>>, i: Int, j: Int) : Int {
        var burnt = 0
        for (x in (-1 .. 1)) {
            for (y in (-1 .. 1)) {
                if (isBurnable(map, i + x, j + y)) {
                    if (burnTile(map, i + x, j + y, x, y))
                        burnt++
                }
            }
        }
        map[i][j] = BURNT
        return burnt
    }

    private fun burnTile(map: MutableList<MutableList<Int>>, mPos: Int, nPos: Int, mDir: Int, nDir: Int) : Boolean {
        return if (nextInt(10) >= 3) {
            map[mPos][nPos] = FIRE
            true
        } else {
            false
        }
    }

    private fun isBurnable(map: MutableList<MutableList<Int>>, i: Int, j: Int) : Boolean {
        return if (isInMap(map, i, j))
            map[i][j] == TREE
        else
            false
    }

    private fun isInMap(map: MutableList<MutableList<Int>>, i: Int, j: Int): Boolean {
        return i < map.size && j < map[0].size && i >= 0 && j >= 0
    }

    private fun readMap(): MutableList<MutableList<Int>> {
        val map = mutableListOf<MutableList<Int>>()
        val lines = getResourceAsText("/maps/basemap.txt")?.split("\n")
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