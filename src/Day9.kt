import java.io.BufferedReader
import java.io.File

class Day9 {

    fun run(fileName: String) {
        val diskData = createData(fileName)
        println(calculate(rearange(diskData)))
    }

    fun run2(fileName: String) {
        val diskData = createData(fileName)
        println(calculate(rearange2(diskData)))
    }

    private fun createData(fileName: String): MutableList<MutableList<String>> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader()
        val map: MutableList<MutableList<String>> = mutableListOf()
        var isNumber = true
        var index = 0
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    var splittedLine = line.map { it.toString() }.toTypedArray()
                    splittedLine.forEach { item -> run {
                        var repeated: MutableList<String>
                        if (isNumber) {
                            repeated = MutableList(item.toInt()) { index.toString() }
                            index++
                        } else {
                            repeated = MutableList(item.toInt()) { "." }
                        }
                        map.add(repeated)
                        isNumber = !isNumber
                    } }
                }
            }
        }
        return map
    }

    private fun rearange(diskData: MutableList<MutableList<String>>): MutableList<MutableList<String>> {
        var indexToUpdate = 0
        var indexToMove = diskData.size - 1
        var subIndexToUpdate = 0
        var subIndexToMove = diskData[indexToMove].size - 1

        while (indexToUpdate < indexToMove) {
            while (subIndexToUpdate < diskData[indexToUpdate].size) {
                if (diskData[indexToUpdate][subIndexToUpdate] == ".") {
                    while (subIndexToMove > -1) {
                        if (diskData[indexToMove][subIndexToMove] == ".") {
                            subIndexToMove--
                        } else {
                            diskData[indexToUpdate][subIndexToUpdate] = diskData[indexToMove][subIndexToMove]
                            diskData[indexToMove][subIndexToMove] = "."
                            subIndexToMove--
                            break
                        }
                    }
                    if (subIndexToMove == -1) {
                        indexToMove--
                        subIndexToMove = diskData[indexToMove].size - 1
                        break
                    }
                } else {
                    subIndexToUpdate++
                    break
                }
            }
            if (subIndexToUpdate == diskData[indexToUpdate].size) {
                indexToUpdate++
                subIndexToUpdate = 0
            }
        }
        return diskData
    }

    private fun rearange2(diskData: MutableList<MutableList<String>>): MutableList<MutableList<String>> {
        var indexToMove = diskData.size - 1

        while (0 < indexToMove) {
            while (diskData[indexToMove].size > 0 && diskData[indexToMove][0] == ".") {
                indexToMove--
            }

            var indexToUpdate = 0
            while (indexToUpdate<indexToMove && indexToMove>-1) {
                if (diskData[indexToUpdate].size>0 && diskData[indexToMove].size>0 && diskData[indexToUpdate][0] == "." && diskData[indexToUpdate].size>=diskData[indexToMove].size) {
                    break
                }
                indexToUpdate++
            }

            if (indexToUpdate < indexToMove) {
                if (diskData[indexToUpdate].size==diskData[indexToMove].size) {
                    diskData[indexToUpdate] = MutableList(diskData[indexToMove].size) { diskData[indexToMove][0] }
                    diskData[indexToMove] = MutableList(diskData[indexToMove].size) { "." }
                } else {
                    val moveSize = diskData[indexToMove].size
                    val updatedSize = diskData[indexToUpdate].size
                    val currentValue = diskData[indexToMove][0]
                    diskData[indexToMove] = MutableList(moveSize) { "." }
                    diskData[indexToUpdate] = MutableList(updatedSize - moveSize) { "." }
                    diskData.add(indexToUpdate, MutableList(moveSize) { currentValue })
                }
            }
            indexToMove--
        }
        return diskData
    }

    private fun calculate(diskData: MutableList<MutableList<String>>): Long {
        var index = 0L
        var result = 0L
        diskData.forEach { item ->
            item.forEach { char ->
               if (char != ".") {
                   result += index * char.toLong(10)
                   index++
               } else {
                   index++
               }
            }
        }
        return result
    }
}


fun main(args: Array<String>) {
    println("Running the main function");
    Day9().run("resource/day9.txt");
    Day9().run2("resource/day9.txt");
}