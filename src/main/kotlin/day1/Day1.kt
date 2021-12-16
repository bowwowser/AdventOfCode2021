package day1

import AoCDaySolution
import printSolution
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day1().solution() }
    println("Both parts solved in ${solutionTimeMillis}ms")
}

class Day1(
    override val inputFilePath: String = "src/main/kotlin/day1/input.txt"
) : AoCDaySolution {
    override fun solution() {
        // Get measurements as List<Int>
        val measurements = getFileLines().map { it.toInt() }.toList()
        val part1Answer = processMeasurement(measurements)
        val part2Answer = processMeasurement(measurements, windowSize = 3)

        printSolution(listOf(part1Answer, part2Answer)) { it.toString() }
    }

    private fun processMeasurement(measurements: List<Int>, windowSize: Int = 1): Int {
        var previousMeasurement = Int.MAX_VALUE
        var measurementIncreases = 0

        for (window in measurements.windowed(size = windowSize)) {
            val currentMeasurement = window.sum()
            if (previousMeasurement < currentMeasurement) {
                measurementIncreases++
            }
            previousMeasurement = currentMeasurement
        }
        return measurementIncreases
    }
}
