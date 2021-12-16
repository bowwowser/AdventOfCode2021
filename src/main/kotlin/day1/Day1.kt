package day1

import java.io.File
import kotlin.streams.toList

fun main() {
    val inputFile = "src/main/kotlin/day1/input.txt"
    val fileBuffer = File(inputFile).bufferedReader()

    // Get measurements as List<Int>
    val measurements = fileBuffer.lines().map { it.toInt() }.toList()
    val part1Answer = processMeasurement(measurements)
    val part2Answer = processMeasurement(measurements, windowSize = 3)

    listOf(part1Answer, part2Answer).forEachIndexed { index, answer ->
        println("Answer for part ${index+1}: $answer")
    }
}

fun processMeasurement(measurements: List<Int>, windowSize: Int = 1): Int {
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
