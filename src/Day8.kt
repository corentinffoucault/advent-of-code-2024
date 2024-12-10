import java.io.BufferedReader
import java.io.File

class Day8 {

    private var x_size = 0
    private var y_size = 0

    fun run(fileName: String) {
        val map = createData(fileName)
        x_size = map[0].size
        y_size = map.size
        val groupedAntenna = parseGrid(map)
        println("result V1 ${countAntiNodes1(groupedAntenna)}")
        println("result V2 ${countAntiNodes2(groupedAntenna)}")
    }

    private fun createData(fileName: String): Array<Array<String>> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        val map: MutableList<Array<String>> = mutableListOf()
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    map.add(line.map { it.toString() }.toTypedArray())
                }
            }
        }
        return map.toTypedArray()
    }

    private fun countAntiNodes1(groupedAntenna: Map<String, List<Point2D>>): Int {
        var uniqueAntiNode = setOf<Point2D>()
        groupedAntenna.forEach { (key, antenna) ->
            antenna.forEachIndexed { i, a ->
                antenna.drop(i + 1).forEach { b ->
                    uniqueAntiNode = uniqueAntiNode union antiNodesForPart1(a, b, a - b)
                }
            }
        }
        return uniqueAntiNode.size
    }

    private fun countAntiNodes2(groupedAntenna: Map<String, List<Point2D>>): Int {
        var uniqueAntiNode = setOf<Point2D>()

        groupedAntenna.forEach { (key, antenna) ->
            antenna.forEachIndexed { i, a ->
                antenna.drop(i + 1).forEach { b ->
                    uniqueAntiNode = uniqueAntiNode union antiNodesForPart2(a, a - b)
                }
            }
        }

        return uniqueAntiNode.size
    }

    private fun antiNodesForPart1(a: Point2D, b: Point2D, diff: Point2D): Set<Point2D> {
        val antiNodeSet = mutableSetOf<Point2D>()
        val antiNode1: Point2D
        val antiNode2: Point2D
        if (a.y > b.y) {
            antiNode1 = a - diff
            antiNode2 = b + diff
        } else {
            antiNode1 = a + diff
            antiNode2 = b - diff
        }
        if (isOnGrid(antiNode1)) {
            antiNodeSet.add(antiNode1)
        }
        if (isOnGrid(antiNode2)) {
            antiNodeSet.add(antiNode2)
        }
        return antiNodeSet
    }

    private fun antiNodesForPart2(a: Point2D, diff: Point2D): Set<Point2D> {
        val antiNodeSet = mutableSetOf<Point2D>()
        var currentNode = a
        while (isOnGrid(currentNode)) {
            antiNodeSet.add(currentNode)
            currentNode -= diff
        }
        currentNode = a
        while (isOnGrid(currentNode)) {
            antiNodeSet.add(currentNode)
            currentNode += diff
        }

        return antiNodeSet
    }

    private fun isOnGrid(point: Point2D): Boolean {
        return point.y < y_size && point.y >-1 && point.x < x_size && point.x >-1
    }

    private fun parseGrid(map: Array<Array<String>>): Map<String, List<Point2D>> {
        val antennas = mutableMapOf<String, MutableList<Point2D>>()
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != ".") {
                    if (!antennas.contains(c)) {
                        antennas.set(c, mutableListOf())
                    }
                    antennas.getValue(c).add(Point2D(x, y))
                }
            }
        }
        return antennas
    }

}


fun main(args: Array<String>) {
    println("Running the main function");
    Day8().run("resource/day8.txt");
}