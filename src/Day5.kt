import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Day5 {

    fun run(fileName: String) {
        val (orders, linesToAnalyses) = createData(fileName);
        val (result, correctedResult) = analyseAllLine(orders, linesToAnalyses)
        println("valid Result $result")
        println("corrected Result $correctedResult")
    }

    private fun createData(fileName: String): Pair<Set<String>, Array<String>> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        val linesToAnalyses: MutableList<String> = mutableListOf()
        val orders: MutableSet<String> = mutableSetOf()
        var isInPartOrder = true
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    if (isInPartOrder) {
                        if (line == "") {
                            isInPartOrder = false
                        } else {
                            orders.add(line)
                        }
                    } else {
                        linesToAnalyses.add(line)
                    }
                }
            }
        };
        return Pair(orders, linesToAnalyses.toTypedArray())
    }

    private fun analyseAllLine(orders: Set<String>, linesToAnalyses: Array<String>): Pair<Int, Int> {
        var result = 0
        var correctedResult = 0
        linesToAnalyses.forEach { line ->
            run {
                val numberForLine = validNumber(orders, line)
                if (numberForLine < 0) {
                    correctedResult += numberForLine
                } else {
                    result += numberForLine
                }
            }
        }
        return Pair(result, abs(correctedResult))
    }

    private fun validNumber(orders: Set<String>, lineToAnalyse: String): Int {
        val splittedLine = lineToAnalyse.split(",")
        splittedLine.forEachIndexed { index, item1 ->
            run {
                if (index < splittedLine.size - 1) {
                    splittedLine.subList(index + 1, splittedLine.size).forEach { item2 ->
                        run {
                            if (orders.contains("$item2|$item1")) {
                                val reorderedArray = reorder(orders, splittedLine)
                                return -reorderedArray[reorderedArray.size / 2].toInt()
                            }
                        }
                    }
                }
            }
        }
        return splittedLine[splittedLine.size / 2].toInt()
    }

    private fun reorder(orders: Set<String>, splittedLine: List<String>): Array<String> {
        val newList: MutableList<String> = mutableListOf()
        val oldList: MutableList<String> = splittedLine.toMutableList()

        while (newList.size < splittedLine.size) {
            if (oldList.size == 1) {
                break
            }
            var index = 0
            while (index < oldList.size) {
                if (checkIsFirst(orders, oldList[index], oldList)) {
                    break
                }
                index++
            }
            newList.add(oldList[index])
            oldList.removeAt(index)
        }
        return newList.toTypedArray()
    }

    private fun checkIsFirst(orders: Set<String>, item: String, splittedLine: List<String>): Boolean {
        var isFirst = true
        splittedLine.forEach { item1 ->
            run {
                if (orders.contains("$item1|$item")) {
                    isFirst = false
                }
            }
        }
        return isFirst
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day5().run("resource/day5.txt");
}