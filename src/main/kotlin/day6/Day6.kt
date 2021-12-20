package day6

import AoCDaySolution
import printSolution
import java.io.File
import kotlin.system.measureTimeMillis

const val DAYS_TO_OBSERVE = 80

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day6().solution() }
//    println("Both parts solved in ${solutionTimeMillis}ms")
    println("First part solved in ${solutionTimeMillis}ms")
}

class Day6(
    override val inputFilePath: String = "src/main/kotlin/day6/input.txt"
) : AoCDaySolution {
    override fun solution() {
        val initialLanternfish = File(inputFilePath).bufferedReader()
            .readLine()
            .split(",")
            .map { Lanternfish(it.toInt()) }

        val part1Answer = getLanternfishCount(initialLanternfish)
        printSolution(listOf(part1Answer)) { it.toString() }
    }
}

fun getLanternfishCount(lanternfish: List<Lanternfish>): Int {
    val currentFish = lanternfish.toMutableList()
    for (day in 1..DAYS_TO_OBSERVE) {
        val spawnedFish = mutableListOf<Lanternfish>()
        for (fish in currentFish) {
            fish.internalTimer--
            if (fish.internalTimer < 0) {
                fish.resetInternalTimer()
                val newFish = fish.spawnNewFish()
                spawnedFish.add(newFish)
            }
        }
        currentFish.addAll(spawnedFish)
//            println("-- Day $day: ${currentFish.map { it.internalTimer }.joinToString()}")
    }

    return currentFish.size
}

const val RESET_TIMER = 6
const val FRESH_FISH_TIMER = 8

data class Lanternfish(
    var internalTimer: Int
) {
    fun resetInternalTimer() {
        internalTimer = RESET_TIMER
    }

    fun spawnNewFish() = Lanternfish(FRESH_FISH_TIMER)
}