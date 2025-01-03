import java.io.BufferedReader
import java.io.File
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

class Robot(var initialPoint: Point2D, private val speed: Point2D) {
    fun solve(size: Point2D): Point2D {
        initialPoint = Point2D(Math.floorMod(initialPoint.x+speed.x,size.x), Math.floorMod(initialPoint.y+speed.y,size.y))
        return initialPoint
    }

    fun getQuadrant(pos: Point2D, size: Point2D): Int {
        if (pos.x < floor(size.x.toDouble()/2)) {
            if (pos.y < floor(size.y.toDouble()/2)) {
                return 1
            } else if (pos.y >= ceil(size.y.toDouble()/2)) {
                return 2
            }
        } else if (pos.x >= ceil(size.x.toDouble()/2)) {
            if (pos.y < floor(size.y.toDouble()/2)) {
                return 3
            } else if (pos.y >= ceil(size.y.toDouble()/2)) {
                return 4
            }
        }
        return 0
    }
}

class Day14 {

    fun run(fileName: String) {
        val size = Point2D(101, 103)
        val robots = createData(fileName, size)
        var quadrants = mutableListOf(0,0,0,0,0)
        File("day14Res.txt").printWriter().use { out ->
            for (i in 0..10_000) {
                val map = createMap(size);
                robots.forEach {
                    map[it.initialPoint.y].setCharAt(it.initialPoint.x, '1')
                }
                out.println("####################$i")
                map.forEach {
                    if (it.contains("1111111111111111111111111111111")) {
                        println("´´´´´´´´´´´´´´´´´´' $i")
                    }
                    out.println(it)
                }
                out.println("############################################################ $i")
                quadrants = mutableListOf(0,0,0,0,0)
                robots.forEach {
                    val currentPoint = it.solve(size)
                    quadrants[it.getQuadrant(currentPoint, size)]++
                }
            }

        }

        println("final result ${quadrants.subList(1, quadrants.size).fold(1) { acc, i -> acc*i}}")


    }

    private fun createMap(size: Point2D): MutableList<StringBuilder> {
        var map = mutableListOf<StringBuilder>()
        for (i in 0..size.y) {
            map.add(StringBuilder(".".repeat(size.x)))
        }
        return map
    }

    private fun createData(fileName: String, size: Point2D): List<Robot> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val robots = mutableListOf<Robot>()
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    val reg = Regex("p=([0-9]+),([0-9]+) v=(-?[0-9]+),(-?[0-9]+)")
                    val res = reg.find(line)
                    val x0 = res?.groups?.get(1)?.value?.toInt()
                    val y0 = res?.groups?.get(2)?.value?.toInt()
                    val cx = res?.groups?.get(3)?.value?.toInt()
                    val cy = res?.groups?.get(4)?.value?.toInt()
                    if (x0 != null && y0 != null && cx != null && cy != null) {
                        robots.add(Robot(Point2D(Math.floorMod(x0,size.x), Math.floorMod(y0,size.y)), Point2D(cx, cy)))
                    }
                }
            }
        }
        return robots
    }

}

fun main(args: Array<String>) {
    println("Running the main function");
    Day14().run("resource/day14.txt")
}