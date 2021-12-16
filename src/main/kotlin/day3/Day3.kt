package day3

import AoCDaySolution
import printSolution
import java.io.File
import java.io.RandomAccessFile
import kotlin.system.measureTimeMillis

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day3().solution() }
    println("First part solved in ${solutionTimeMillis}ms")
}

class Day3(
    override val inputFilePath: String = "src/main/kotlin/day3/input.txt"
) : AoCDaySolution {

    override fun solution() {
        val inputFileRaf = RandomAccessFile(File(inputFilePath), "r")
        val arraySize = inputFileRaf.readLine().length // get size from length of first line
        inputFileRaf.seek(0)
        val oneCounts = IntArray(arraySize)
        val zeroCounts = IntArray(arraySize)

        getInputAsLines().forEach {
            it.forEachIndexed { index, bitChar ->
                if (bitChar == '1') oneCounts[index]++ else zeroCounts[index]++
            }
        }

        var gammaString = ""
        var epsilonString = ""
        for (i in 0 until arraySize) {
            if (oneCounts[i] > zeroCounts[i]) {
                gammaString += "1"
                epsilonString += "0"
            } else {
                gammaString += "0"
                epsilonString += "1"
            }
        }

        val part1Answer = Pair(gammaString.toInt(2), epsilonString.toInt(2))

        printSolution(listOf(part1Answer)) {
            (it.first * it.second).toString()
        }
    }
}
