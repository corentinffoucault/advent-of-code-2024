import java.io.BufferedReader
import java.io.File
import kotlin.math.abs

class Day2 {

    fun run(fileName: String) {
        var nbSafe = 0

        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        bufferedReader.use {
            it.forEachLine {
                line: String -> run {
                    nbSafe += (if (isSafe(line)) 1 else 0)
                }
            }
        };

        println(nbSafe)

    }

    private fun isSafe(line: String): Boolean {

        var splittedLine = line.split(" ")
        if (splittedLine.toSet().size<(splittedLine.size-1)) {
            return false
        }

        for (index in -1..splittedLine.size){
            if(isSafeList(splittedLine, index).isEmpty()) {
                return true
            }
        }

        return false
    }
//
//    private fun isSafe(line: String): Boolean {
//        var splittedLine = line.split(" ")
//        var setList = splittedLine.toSet()
//        if (setList.size<(splittedLine.size-2)) {
//            return false
//        }
//        var indexInError = isSafeList(splittedLine, -1)
//        if (indexInError.isEmpty()) {
//            return true
//        }
//
//        if (indexInError.size>2 && indexInError.size<(splittedLine.size-2)) {
//            return false
//        }
//
//        if (indexInError.size ==2 && abs(indexInError[1]-indexInError[0]) != 1) {
//            return false
//        }
//        if (isSafeList(splittedLine, indexInError[0]).isEmpty()) {
//            return true
//        }
//
//        if (isSafeList(splittedLine, indexInError[0]+1).isEmpty()) {
//            return true
//        }
//        if (indexInError.size ==2 && isSafeList(splittedLine, indexInError[1]).isEmpty()) {
//            return true
//        }
//
//        return false
//    }

    private fun isSafeList(splittedLine: List<String>, dropIndex: Int): List<Int> {
        var currentList = splittedLine;
        if (dropIndex!=-1) {
            currentList = splittedLine.filterIndexed { index, _ ->  index!=dropIndex }
        }
        var order = 0
        var indexInError = mutableListOf<Int>()
        for (index in 0..currentList.size - 2) {
            val previousItem = currentList[index].toInt()
            val currentItem = currentList[index + 1].toInt()

            val gap = abs(previousItem - currentItem);
            if (gap > 3 || gap < 1) {
                indexInError.add(index)
            }

            if (order == 0) {
                order = if (previousItem > currentItem) 1 else -1
                continue
            }

            if (previousItem < currentItem && order == 1 ||
                previousItem > currentItem && order == -1) {
                indexInError.add(index)
            }
        }

        return indexInError
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day2().run("resource/day2.txt");
}