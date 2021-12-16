package day2

import AoCDaySolution
import printSolution
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day2().solution() }
    println("Both parts solved in ${solutionTimeMillis}ms")
}

class Day2(
    override val inputFilePath: String = "src/main/kotlin/day2/input.txt"
) : AoCDaySolution {
    override fun solution() {
        // Get measurements as List<Instruction>
        val instructions = getFileLines()
            .map { Instruction(it.split(" ")) }
            .toList()
        val part1Answer = Part1Submarine(instructions).processInstructions()
        val part2Answer = Part2Submarine(instructions).processInstructions()

        printSolution(listOf(part1Answer, part2Answer)) { answer ->
            (answer.first * answer.second).toString()
        }
    }
}

data class Instruction(val command: String, val units: Int) {
    constructor(commandPair: List<String>) : this(commandPair[0], commandPair[1].toInt())
}

abstract class Submarine(
    private val instructions: List<Instruction>
) {

    protected var hPosition: Int = 0
    protected var depth: Int = 0
    protected var aim: Int = 0

    fun processInstructions(): Pair<Int, Int> {
        for (instruction in instructions) {
            when (instruction.command) {
                "forward" -> forward(instruction.units)
                "down" -> down(instruction.units)
                "up" -> up(instruction.units)
            }
        }

        return Pair(hPosition, depth)
    }

    abstract fun forward(units: Int)
    abstract fun down(units: Int)
    abstract fun up(units: Int)
}

class Part1Submarine(
    instructions: List<Instruction>
) : Submarine(instructions) {

    override fun forward(units: Int) {
        hPosition += units
    }

    override fun down(units: Int) {
        depth += units
    }

    override fun up(units: Int) {
        depth -= units
    }
}

class Part2Submarine(
    instructions: List<Instruction>
) : Submarine(instructions) {

    override fun forward(units: Int) {
        hPosition += units
        depth += aim * units
    }

    override fun down(units: Int) {
        aim += units
    }

    override fun up(units: Int) {
        aim -= units
    }
}
