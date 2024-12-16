import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Garden(private val groupedFlowers: Map<Char, Set<Point2D>>) {

    fun calculateFullCost(): Long {
        var result = 0L
        groupedFlowers.forEach { (c, group) -> run {
            val continuousGroup = splitFlowerInContinuousGroup(group)
            result += calculateForOneCategorie(c, continuousGroup)
        } }
        return result
    }

    fun calculateFullCostV2(): Long {
        var result = 0L
        groupedFlowers.forEach { (c, group) -> run {
            val continuousGroup = splitFlowerInContinuousGroup(group)
            result += calculateForOneCategorieV2(c, continuousGroup)
        } }
        return result
    }

    private fun calculateForOneCategorie(cat: Char, continuousGroups: Set<Set<Point2D>>): Long {
        var tmp = 0L
        continuousGroups.forEach{ continuousGroup -> run {
            val a = fenceSize(continuousGroup)
            tmp += continuousGroup.size * a
        } }
        return tmp
    }

    private fun calculateForOneCategorieV2(cat: Char, continuousGroups: Set<Set<Point2D>>): Long {
        var tmp = 0L
        continuousGroups.forEach{ continuousGroup -> run {
            val a = fenceSizeV2(cat, continuousGroup)
            tmp += continuousGroup.size * a
        } }
        return tmp
    }

    private fun splitFlowerInContinuousGroup(flowers: Set<Point2D>): Set<Set<Point2D>> {
        var groupedSeen = mutableSetOf<Set<Point2D>>()

        flowers.forEach { currentFlower -> run {
            val contiguous = mutableSetOf<Set<Point2D>>()
            val nonContiguous = mutableSetOf<Set<Point2D>>()
            groupedSeen.forEach { if (isContiguous(it, currentFlower)) contiguous.add(it) else nonContiguous.add(it) }
            nonContiguous.add(fusionContiguous(contiguous, currentFlower))
            groupedSeen = nonContiguous
        } }

        return groupedSeen
    }

    private fun fusionContiguous(contiguous: MutableSet<Set<Point2D>>, currentFlower: Point2D): Set<Point2D> {
        contiguous.add(setOf(currentFlower))
        return contiguous.reduce { acc, point2DS -> acc union point2DS }
    }

    private fun isContiguous(group: Set<Point2D>, currentFlower: Point2D): Boolean {
        group.forEach {
            if (abs(it.x-currentFlower.x)==1 && it.y-currentFlower.y==0 ||
                it.x-currentFlower.x==0 && abs(it.y-currentFlower.y)==1) {
                return true
            }
        }
        return false
    }

    private fun fenceSize(group: Set<Point2D>): Long {
        val fencesByDir = mutableMapOf<Char, MutableSet<Point2D>>()
        fencesByDir['^'] = mutableSetOf()
        fencesByDir['>'] = mutableSetOf()
        fencesByDir['<'] = mutableSetOf()
        fencesByDir['v'] = mutableSetOf()
        group.forEach{ flower -> nbFenceAroundFlower(group, flower, fencesByDir) }

        var res = 0L
        fencesByDir.forEach { (dir, value) -> run { res += value.size } }
        // println("######## ${group.size} ${fencesByDir['^']?.size} ${fencesByDir['>']?.size} ${fencesByDir['v']?.size} ${fencesByDir['<']?.size}")
        return res;
    }

    private fun fenceSizeV2(cat: Char, group: Set<Point2D>): Long {
        val fencesByDir = mutableMapOf<Char, MutableSet<Point2D>>()
        fencesByDir['^'] = mutableSetOf()
        fencesByDir['>'] = mutableSetOf()
        fencesByDir['<'] = mutableSetOf()
        fencesByDir['v'] = mutableSetOf()
        group.forEach{ flower -> nbFenceAroundFlower(group, flower, fencesByDir) }

        var res = 0L

        fencesByDir.forEach{(key, value) -> run {
            res += splitFlowerInContinuousGroup(cat, key, value)
        } }

        // println("######## ${group.size} ${fencesByDir['^']?.size} ${fencesByDir['>']?.size} ${fencesByDir['v']?.size} ${fencesByDir['<']?.size}")
        return res;
    }

    private fun splitFlowerInContinuousGroup(cat: Char, dir: Char, flowers: MutableSet<Point2D>): Int {
        var groupedSeen = mutableSetOf<Set<Point2D>>()

        flowers.forEach { currentFlower -> run {
            val contiguous = mutableSetOf<Set<Point2D>>()
            val nonContiguous = mutableSetOf<Set<Point2D>>()
            groupedSeen.forEach { if (isContiguous(it, currentFlower)) contiguous.add(it) else nonContiguous.add(it) }
            nonContiguous.add(fusionContiguous(contiguous, currentFlower))
            groupedSeen = nonContiguous

        } }

        return groupedSeen.size
    }

    private fun nbFenceAroundFlower(group: Set<Point2D>, flower: Point2D, fencesByDir: MutableMap<Char, MutableSet<Point2D>>): Long {
        if (!group.contains(flower+Point2D(1,0))) {
            fencesByDir['>']?.add(flower+Point2D(1,0))
        }
        if (!group.contains(flower+Point2D(-1,0))) {
            fencesByDir['<']?.add(flower+Point2D(-1,0))
        }
        if (!group.contains(flower+Point2D(0,1))) {
            fencesByDir['v']?.add(flower+Point2D(0, 1))
        }
        if (!group.contains(flower+Point2D(0,-1))) {
            fencesByDir['^']?.add(flower+Point2D(0, -1))
        }
        return 0L
    }
}

class Day12 {

    fun run(fileName: String) {
        val garden = createData(fileName)
        println("final result ${garden.calculateFullCost()}")
        println("final resultV2 ${garden.calculateFullCostV2()}")
    }

    private fun createData(fileName: String): Garden {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val groupedFlowers = mutableMapOf<Char, MutableSet<Point2D>>()
        var y = 0;
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    val splittedLine = line.map { value -> run { value } }
                    splittedLine.forEachIndexed { x, item -> run {
                        if (!groupedFlowers.contains(item)) {
                            groupedFlowers[item] = mutableSetOf()
                        }
                        groupedFlowers[item]?.add(Point2D(x, y))
                    } }
                }
                y++
            }
        }
        return Garden(groupedFlowers)
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day12().run("resource/day12.txt")
}