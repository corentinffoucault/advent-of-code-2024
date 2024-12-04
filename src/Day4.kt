import java.io.BufferedReader
import java.io.File

class Day4 {

    fun run(fileName: String) {
        val matrix = createMatrix(fileName);
        println(findXMas(matrix))
    }

    fun run2(fileName: String) {
        val matrix = createMatrix(fileName);
        println(findCrossMas(matrix))
    }

    private fun createMatrix(fileName: String): Array<Array<String>> {
        val bufferedReader: BufferedReader = File(fileName).bufferedReader();
        val matrix: MutableList<Array<String>> = mutableListOf()
        bufferedReader.use {
            it.forEachLine { line: String ->
                run {
                    matrix.add(createLine(line))
                }
            }
        };
        return matrix.toTypedArray()
    }

    private fun createLine(line: String): Array<String> {
        return line.lowercase().map { it.toString() }.toTypedArray()
    }

    private fun findCrossMas(matrix: Array<Array<String>>): Int {
        var result = 0
        matrix.forEachIndexed { indexLine, line ->
            run {
                line.forEachIndexed { indexCol, item ->
                    run {
                        if (charIsA(item)) {
                            result+=findMasLimit(matrix, indexLine, indexCol)
                        }
                    }
                }
            }
        }
        return result;
    }

    private fun findXMas(matrix: Array<Array<String>>): Int {
        var result = 0
        matrix.forEachIndexed { indexLine, line ->
            run {
                line.forEachIndexed { indexCol, item ->
                    run {
                        if (charIsX(item)) {
                            result+=findWordLimit(matrix, indexLine, indexCol)
                        }
                    }
                }
            }
        }
        return result;
    }

    private fun charIsX(character: String): Boolean {
        return character == "x"
    }

    private fun charIsA(character: String): Boolean {
        return character == "a"
    }

    private fun findWordLimit(matrix: Array<Array<String>>, indexLine: Int, indexCol: Int): Int {
        var result = 0
        if (indexLine > 2) {
            if (indexCol > 2) {
                result += findWord(matrix, indexLine, indexCol, -1, -1)
            }
            if (indexCol < matrix[0].size - 3) {
                result += findWord(matrix, indexLine, indexCol, -1, 1)
            }
            result += findWord(matrix, indexLine, indexCol, -1, 0)
        }

        if (indexLine < matrix.size - 3) {
            if (indexCol > 2) {
                result += findWord(matrix, indexLine, indexCol, 1, -1)
            }
            if (indexCol < matrix[0].size - 3) {
                result += findWord(matrix, indexLine, indexCol, 1, 1)
            }
            result += findWord(matrix, indexLine, indexCol, 1, 0)
        }

        if (indexCol > 2) {
            result += findWord(matrix, indexLine, indexCol, 0, -1)
        }
        if (indexCol < matrix[0].size - 3) {
                result += findWord(matrix, indexLine, indexCol, 0, 1)
        }
        println("${indexLine} ${indexCol} ${matrix[indexLine][indexCol]} ${result}")
        return result
    }

    private fun findMasLimit(matrix: Array<Array<String>>, indexLine: Int, indexCol: Int): Int {
        if (indexLine > 0 && indexLine < matrix.size - 1 && indexCol > 0 && indexCol < matrix[0].size - 1) {
            if (matrix[indexLine-1][indexCol-1]=="m" && matrix[indexLine+1][indexCol+1]=="s" ||
                matrix[indexLine-1][indexCol-1]=="s" && matrix[indexLine+1][indexCol+1]=="m") {
                if (matrix[indexLine-1][indexCol+1]=="m" && matrix[indexLine+1][indexCol-1]=="s" ||
                    matrix[indexLine-1][indexCol+1]=="s" && matrix[indexLine+1][indexCol-1]=="m") {
                    println("$indexLine $indexCol")
                        return 1
                }
            }
        }
        return 0
    }

    private fun findWord(matrix: Array<Array<String>>, indexLine: Int, indexCol: Int, incrLine: Int, incrCol: Int): Int {
        var currentLine = indexLine+incrLine
        var currentCol = indexCol+incrCol
        if (matrix[currentLine][currentCol]=="m") {
            currentLine += incrLine
            currentCol += incrCol
            if (matrix[currentLine][currentCol]=="a") {
                currentLine += incrLine
                currentCol += incrCol
                if (matrix[currentLine][currentCol]=="s") {

                    return 1
                }
            }
        }
        return 0
    }
}

fun main(args: Array<String>) {
    println("Running the main function");
    Day4().run2("resource/day4.txt");
}