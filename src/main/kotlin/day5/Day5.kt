package day5

import AoCDaySolution
import printSolution
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day5().solution() }
    println("Both parts solved in ${solutionTimeMillis}ms")
}

class Day5(
    override val inputFilePath: String = "src/main/kotlin/day5/input.txt"
) : AoCDaySolution {
    override fun solution() {
        val ventLines = getInputAsLines().toList()
            .map { line ->
                val startPoint = line.split(" -> ")[0].split(",").map { it.toInt() }
                val endPoint = line.split(" -> ")[1].split(",").map { it.toInt() }
                VentLine(
                    Point(startPoint[0], startPoint[1]),
                    Point(endPoint[0], endPoint[1]))
            }

        val part1Answer = findPointsWithOverlap(ventLines.filter { it.isHorizontal() || it.isVertical() })
        val part2Answer = findPointsWithOverlap(ventLines)
        printSolution(listOf(part1Answer, part2Answer)) { answer ->
            answer.toString()
        }
    }
}

fun findPointsWithOverlap(ventLines: List<VentLine>): Int {
    val orthogonalCoverage = mutableMapOf<Point, Int>()
    ventLines.forEach { line ->
        line.coveredPoints()
            .forEach { point ->
                val coverage = orthogonalCoverage.getOrPut(point) { 0 }
                orthogonalCoverage[point] = coverage + 1
            }
    }
//    orthogonalCoverage.forEach { (t, u) -> println("$t => $u") }
    return orthogonalCoverage.filter { it.value > 1 }.count()
}

data class VentLine(val start: Point, val end: Point) {

    fun isHorizontal() = start.x == end.x

    fun isVertical() = start.y == end.y

    fun coveredPoints(): List<Point> {
        val pointsCovered = mutableListOf<Point>()
        if (isHorizontal()) {
            for (y in min(start.y, end.y)..max(start.y, end.y)) {
                pointsCovered.add(Point(start.x, y))
            }
        } else if (isVertical()) {
            for (x in min(start.x, end.x)..max(start.x, end.x)) {
                pointsCovered.add(Point(x, start.y))
            }
        } else {
            // diagonal at 45; safe assumption in puzzle
            val xStep = if (start.x < end.x) 1 else -1
            val yStep = if (start.y < end.y) 1 else -1

            var currentX = start.x
            var currentY = start.y
            while (currentX != end.x) {
                pointsCovered.add(Point(currentX, currentY))
                currentX += xStep
                currentY += yStep
            }
            pointsCovered.add(Point(end.x, end.y))
        }
        return pointsCovered
     }
}

data class Point(val x: Int, val y: Int)