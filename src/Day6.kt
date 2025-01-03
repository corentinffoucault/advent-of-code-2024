import java.io.BufferedReader
import java.io.File

class Player(val map: Array<Array<String>>, val coordInit: Point2D) {

    fun run(): MutableSet<Point2D> {

        var dir = Point2D(0, -1)
        val seenCase = mutableSetOf<Point2D>()
        var coord = coordInit
        seenCase += coord
        var nextCoord = coord + dir
        while (nextCoord.x < map[0].size && nextCoord.x > -1 && nextCoord.y < map.size && nextCoord.y > -1) {
            if (map[nextCoord.y][nextCoord.x] == "#") {
                dir = rotate(dir)
            } else {
                coord = nextCoord
                seenCase += coord
            }
            nextCoord = coord + dir
        }
        return seenCase
    }

    fun run2(): Int {
        val firstSeen = run()
        var nbLoop = 0
        firstSeen.forEach {
            println(map[it.y][it.x])
            map[it.y][it.x] = "#"
            if (move()) {
                nbLoop++
            }
            map[it.y][it.x] = "."
        }
        return nbLoop
    }

    private fun move(): Boolean {
        var dir = Point2D(0, -1)
        val seenCase = mutableSetOf<Pair<Point2D, Point2D>>()
        var coord = coordInit
        seenCase += coord to dir
        var nextCoord = coord + dir
        while (nextCoord.x < map[0].size && nextCoord.x > -1 && nextCoord.y < map.size && nextCoord.y > -1) {

            if (map[nextCoord.y][nextCoord.x] == "#") {
                dir = rotate(dir)
            } else {
                println("${nextCoord.x}_${nextCoord.y} ${dir.x}_${dir.y} ${seenCase.size}")

                coord = nextCoord
                if ((nextCoord to dir) in seenCase) {
                    return true
                }
                seenCase += coord to dir
            }
            nextCoord = coord + dir
        }
        return false
    }

    private fun rotate(dir: Point2D): Point2D {
        return if (dir.y == 1) {
            Point2D(-1, 0)
        } else if (dir.y == -1) {
            Point2D(1, 0)
        } else if (dir.x == 1) {
            Point2D(0, 1)
        } else {
            Point2D(0, -1)
        }
    }
    private fun getDir(dir: Point2D): String {
        if (dir.y == 1) {
            return "u"
        } else if (dir.y == -1) {
            return "^"
        } else if (dir.x == 1) {
            return ">"
        } else {
            return "<"
        }
    }
    fun print() {
        map.forEach { line -> println(line.joinToString("")) }
    }
}

class Day6 {

    fun run(fileName: String) {
        val player = createData(fileName);
        println("corrected Result ${player.run2()}")
    }

    private fun createData(fileName: String): Player {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        val map: MutableList<Array<String>> = mutableListOf()
        var lineIndex = 0
        var coord = Point2D(0, 0)
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    if (line.contains('^')) {
                        coord = Point2D(line.indexOf('^'), lineIndex)
                    }
                    map.add(line.lowercase().map { it.toString() }.toTypedArray())
                    lineIndex++
                }
            }
        }

        return Player(map.toTypedArray(), coord)
    }
}


fun main(args: Array<String>) {
    println("Running the main function");
    Day6().run("resource/day6.txt");
}