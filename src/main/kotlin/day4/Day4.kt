package day4

import AoCDaySolution
import printSolution
import java.io.File
import java.util.regex.Pattern
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun main() {
    val solutionTimeMillis = measureTimeMillis { Day4().solution() }
    println("First part solved in ${solutionTimeMillis}ms")
}

class Day4(
    override val inputFilePath: String = "src/main/kotlin/day4/input.txt"
) : AoCDaySolution {
    override fun solution() {
        val fileBuffer = File(inputFilePath).bufferedReader()
        val numbers = fileBuffer.readLine()
            .split(",")
            .map { it.toInt() }
            .toIntArray()
        println("Parsed numbers ${numbers.joinToString(",")}")
        // Might need better performance vvv
        val boards = fileBuffer.lines()
            .filter { it.isNotEmpty() }
            .toList()
            .chunked(5) { bingoRows ->
                assert(bingoRows.size == 5)
                Board(bingoRows)
            }

        val game = BingoGame(numbers, boards)
//        println("${game}")

        val part1Answer = calculateScoreForQuickestWinningBoard(game)
        game.resetGame()
//        println("---- Game Reset! ----")
        val part2Answer = calculateScoreForSlowestWinningBoard(game)

        printSolution(listOf(part1Answer, part2Answer)) {
            it.toString()
        }
    }
}

fun calculateScoreForQuickestWinningBoard(game: BingoGame): Int {
    val numbers = game.numbersToDraw

    var lastCall = 0
    for (call in numbers) {
//        println("Calling...[${call}]!")
        lastCall = call
        game.boards.forEachIndexed { index, board ->
            board.markBoard(call)
            if (board.checkIfWinningBoard()) {
                game.winningBoards.add(index)
            }
        }
        if (game.winningBoards.size == 1) break
    }

    return game.boards[game.winningBoards[0]].calculateScore(lastCall)
}

fun calculateScoreForSlowestWinningBoard(game: BingoGame): Int {
    val numbers = game.numbersToDraw

    var lastCall = 0
    for (call in numbers) {
//        println("Calling...[${call}]!")
        lastCall = call
        game.boards.forEachIndexed { index, board ->
            board.markBoard(call)
            if (board.checkIfWinningBoard() && !game.winningBoards.contains(index)) {
                game.winningBoards.add(index)
            }
        }
        if (game.winningBoards.size == game.boards.size) break
    }

    return game.boards[game.winningBoards.last()].calculateScore(lastCall)
}

data class BingoGame(
    val numbersToDraw: IntArray,
    val boards: List<Board>
) {
    var winningBoards = mutableListOf<Int>()

    fun resetGame() {
        boards.forEach { it.clearBoard() }
    }
}

class Board() {
    private val bingoBoard = IntArray(25)
    private var bingoMarks = BooleanArray(25)

    private val winningCombos = listOf(
        (0..4),
        (5..9),
        (10..14),
        (15..19),
        (20..24),
        (20..24),
        (0..20 step 5),
        (1..21 step 5),
        (2..22 step 5),
        (3..23 step 5),
        (4..24 step 5),
    ).map { it.toList().toIntArray() }

    constructor(bingoRows: List<String>) : this() {
        var boardIndex = 0
        bingoRows.forEachIndexed { index, row ->
            row.split(Pattern.compile("\\W+"))
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .forEach {
                    bingoBoard[boardIndex++] = it
                }
        }
    }

    fun markBoard(numberToMark: Int) {
        val numberIndex = bingoBoard.indexOf(numberToMark)
        if (numberIndex != -1) bingoMarks[numberIndex] = true
    }

    fun clearBoard() {
        bingoMarks = BooleanArray(25)
    }

    fun calculateScore(lastCall: Int): Int {
        var score = 0
        bingoBoard.forEachIndexed { index, number ->
            if (!bingoMarks[index]) {
                score += number
            }
        }
        return score * lastCall
    }

    fun checkIfWinningBoard(): Boolean {
        var winFound = false
        for (combo in winningCombos) {
            var isWinningCombo = true
            for (index in combo) {
                isWinningCombo = isWinningCombo and bingoMarks[index]
                if (isWinningCombo) continue else break
            }
            if (isWinningCombo) {
                winFound = true
                break
            } else continue
        }
        return winFound
    }

    override fun toString(): String =
        bingoBoard.toList().chunked(5).joinToString(",\n", prefix = "\nBoard:\n")
}
