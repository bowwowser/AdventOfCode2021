package day6

import AoCDaySolution
import printSolution
import java.io.File
import kotlin.system.measureTimeMillis

const val DAYS_PART1 = 80
const val DAYS_PART2 = 256

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day6().solution() }
    println("Both parts solved in ${solutionTimeMillis}ms")
//    println("First part solved in ${solutionTimeMillis}ms")
}

class Day6(
    override val inputFilePath: String = "src/main/kotlin/day6/input.txt"
) : AoCDaySolution {
    override fun solution() {
        val initialLanternfish = File(inputFilePath).bufferedReader()
            .readLine()
            .split(",")
            .map { Lanternfish(it.toInt()) }

        val part1Answer = getLanternfishCountLarge(initialLanternfish.toList(), DAYS_PART1)
        val part2Answer = getLanternfishCountLarge(initialLanternfish.toList(), DAYS_PART2)
        printSolution(listOf(part1Answer, part2Answer)) { it.toString() }
    }
}

const val RESET_TIMER = 6
const val FRESH_FISH_TIMER = 8

/**
 * Better solution found for part 2. Instead, considers fish with same timer value as a group,
 * and processes / maintains only those group counts.
 */
fun getLanternfishCountLarge(lanternfish: List<Lanternfish>, daysToObserve: Int): Long {
    // Get the initial group counts for the input.
    // Map to Long to avoid overflowing Ints
    val currentCountsByTimer = lanternfish.groupingBy { it.internalTimer }
        .eachCount()
        .mapValues { it.value.toLong() }
        .toMutableMap()
    // Initialize all timer keys for algorithm safety
    (0..FRESH_FISH_TIMER).forEach { currentCountsByTimer.putIfAbsent(it, 0) }
//    println("Counts at start: ${currentCountsByTimer.entries.joinToString()}")

    for (day in 1..daysToObserve) {
        var spawningFish = 0L // maintain these & apply AFTER the rest to avoid double-processing
        for (timerValue in currentCountsByTimer.keys.sorted()) {
            if (timerValue == 0) {
                spawningFish = currentCountsByTimer.getValue(timerValue)
            } else {
                // Ranges are safe here, thanks to the check in the if branch (0<i<size)
                // (Even though this is a map and these are just Int keys)
                currentCountsByTimer[timerValue-1] = currentCountsByTimer.getValue(timerValue)
            }
        }
        // Reset last group count explicitly
        currentCountsByTimer[FRESH_FISH_TIMER] = 0
        // Apply spawning fish now
        currentCountsByTimer.merge(FRESH_FISH_TIMER, spawningFish, Long::plus) // New fish
        currentCountsByTimer.merge(RESET_TIMER, spawningFish, Long::plus) // Spawners reset
//        val sum = currentCountsByTimer.values.sum()
//        println("Counts on day $day: ${currentCountsByTimer.entries.joinToString()} (Total: $sum)")
    }

    return currentCountsByTimer.values.sum()
}

// The methods were only used for part 1, but staying here for reference
data class Lanternfish(var internalTimer: Int)
//{
//    fun resetInternalTimer() {
//        internalTimer = RESET_TIMER
//    }
//
//    fun spawnNewFish() = Lanternfish(FRESH_FISH_TIMER)
//}

/**
 * Initial solution for part 1. Considered each fish individually, and summed at the end.
 */
//fun getLanternfishCountSmall(lanternfish: List<Lanternfish>, daysToObserve: Int): Int {
//    val currentFish = lanternfish.toMutableList()
//    for (day in 1..daysToObserve) {
//        val spawnedFish = mutableListOf<Lanternfish>()
//        for (fish in currentFish) {
//            fish.internalTimer--
//            if (fish.internalTimer < 0) {
//                fish.resetInternalTimer()
//                val newFish = fish.spawnNewFish()
//                spawnedFish.add(newFish)
//            }
//        }
//        currentFish.addAll(spawnedFish)
////            println("-- Day $day: ${currentFish.map { it.internalTimer }.joinToString()}")
//    }
//
//    return currentFish.size
//}
