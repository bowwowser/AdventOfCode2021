package day3

import AoCDaySolution
import printSolution
import java.util.stream.Stream
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day3().solution() }
    println("Both parts solved in ${solutionTimeMillis}ms")
}

class Day3(
    override val inputFilePath: String = "src/main/kotlin/day3/input.txt"
) : AoCDaySolution {

    override fun solution() {
        val part1Answer = getRatingsToCompare(getInputAsLines())
        val part2Answer = getRatingsToCompare(getInputAsLines(), narrowChoices = true)

        printSolution(listOf(part1Answer, part2Answer)) {
            (it.first * it.second).toString()
        }
    }
}

fun getRatingsToCompare(inputAsLines: Stream<String>, narrowChoices: Boolean = false): Pair<Int, Int> {
    val originalDiagnostics = inputAsLines.toList()

    var firstRating = ""
    var secondRating = ""
    if (narrowChoices) {
        firstRating = getOxygenGeneratorRating(originalDiagnostics)
        secondRating = getCO2ScrubberRating(originalDiagnostics)
    } else {
        transposeDiagnostics(originalDiagnostics.map { it.toCharArray() }).forEach { positionChars ->
            val mostCommonBit = findMostCommonBit(positionChars)
            val leastCommonBit = if (mostCommonBit == '0') '1' else '0'

            firstRating += mostCommonBit
            secondRating += leastCommonBit
        }
    }

    return Pair(firstRating.toInt(2), secondRating.toInt(2))
}

fun getOxygenGeneratorRating(originalDiagnostics: List<String>): String {
    var currentChoices = originalDiagnostics

    var bitPositionToCheck = 0
    while (currentChoices.size > 1 || bitPositionToCheck > originalDiagnostics[0].length) {
//        println("Current choices: ${currentChoices.joinToString()}")
        val transposedChoices = transposeDiagnostics(currentChoices.map { it.toCharArray() })
        val mostCommonBit = findMostCommonBit(transposedChoices[bitPositionToCheck])

        val newChoices = currentChoices.filter { it[bitPositionToCheck] == mostCommonBit }
        currentChoices = newChoices
        bitPositionToCheck++
    }

//    println("Last choice: ${currentChoices[0]}")
    return currentChoices[0]
}

fun getCO2ScrubberRating(originalDiagnostics: List<String>): String {
    var currentChoices = originalDiagnostics

    var bitPositionToCheck = 0
    while (currentChoices.size > 1) {
//        println("Current choices: ${currentChoices.joinToString()}")
        val transposedChoices = transposeDiagnostics(currentChoices.map { it.toCharArray() })
        val mostCommonBit = findMostCommonBit(transposedChoices[bitPositionToCheck])

        val newChoices = currentChoices.filter { it[bitPositionToCheck] != mostCommonBit }
        currentChoices = newChoices
        bitPositionToCheck++
    }

//    println("Last choice: ${currentChoices[0]}")
    return currentChoices[0]
}

fun findMostCommonBit(positionChars: CharArray): Char {
    val onesCount = positionChars.count { it == '1' }
    val zeroCount = positionChars.size - onesCount

    return if (onesCount >= zeroCount) '1' else '0'
}

fun transposeDiagnostics(diagInput: List<CharArray>): List<CharArray> {
    val newRowCount = diagInput.size        // new row size = old col size
    val newColCount = diagInput[0].size     // new col size = old row size

    val transposedDiags = List(newColCount) { CharArray(newRowCount) }
    for (col in 0 until newColCount) {
        for (row in 0 until newRowCount) {
            transposedDiags[col][row] = diagInput[row][col]
        }
    }
    return transposedDiags
}