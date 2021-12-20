package day7

import AoCDaySolution
import printSolution
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day7().solution() }
    println("Both parts solved in ${solutionTimeMillis}ms")
//    println("First part solved in ${solutionTimeMillis}ms")
}

class Day7(
    override val inputFilePath: String = "src/main/kotlin/day7/input.txt"
) : AoCDaySolution {
    override fun solution() {
        val crabPositions = getInputLine().split(",").map { it.toInt() }

        val part1Answer = alignCrabs(crabPositions, false)
        val part2Answer = alignCrabs(crabPositions, true)

        printSolution(listOf(part1Answer, part2Answer)) { it.toString() }
    }

}

fun alignCrabs(crabPositions: List<Int>, useCrabEngineering: Boolean): Int {
    val crabCountAtPosition = crabPositions.groupingBy { it }
        .eachCount()
    val furthestPosition = crabPositions.maxOrNull() ?: 0
//    println("Furthest crab at $furthestPosition")

    var totalFuelUsed: Int
    // Naive guess: most common position to start = cheapest
    // Result: 481610 (too high)      in 21ms
//    val mostCommonStartPosition = crabCountAtPosition.maxByOrNull { it.value }?.key ?: 0
//    totalFuelUsed = moveCrabs(crabCountAtPosition, mostCommonStartPosition, useCrabEngineering)

    // Better naive approach: start from 0, find cost to move each group to that position
    // There should be a "focal point" of sorts that we can stop at
    // (decreases, then increases again)
    // Could be slow                  not too bad: 47ms
    var thisCost = Int.MAX_VALUE
    var lastCost = Int.MAX_VALUE
    var destination = 0
    while (destination < furthestPosition && lastCost >= thisCost) {
        lastCost = thisCost
        thisCost = moveCrabs(crabCountAtPosition, destination, useCrabEngineering)
//        println("Cost to move crabs to position $destination: $fuelCost")
        destination++
    }
    totalFuelUsed = lastCost // thisCost would be the increase after the decreases
    // Performance improvement - maintain list of costs and shift costs (Map.merge())
    // Though this would require maintaining original position of crabs too
    // (for part 2 scaling cost)

    return totalFuelUsed
}

fun moveCrabs(countsAtPosition: Map<Int, Int>, startingPosition: Int, useCrabEngineering: Boolean): Int {
    var fuelCost = 0
    for ((position, count) in countsAtPosition.entries) {
        if (position == startingPosition) {
            continue // no need to move
        }
        val distanceToMove = abs(position - startingPosition)
        // Crab engineering: each unit = 1 more fuel cost (step 1: 1 fuel, step 2: 2 fuel, etc.)
        val costToMove = if (useCrabEngineering) {
            (1..distanceToMove).sum()
        } else {
            distanceToMove
        }

        fuelCost += costToMove * count
    }
    return fuelCost
}
