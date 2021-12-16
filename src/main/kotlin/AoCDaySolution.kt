import java.io.File
import java.util.stream.Stream

interface AoCDaySolution {
    val inputFilePath: String

    fun solution()

    fun getInputAsLines(): Stream<String> = File(inputFilePath).bufferedReader().lines()
}

fun <T> printSolution(answers: List<T>, solutionText: (T) -> String) {
    answers.forEachIndexed { index, answer ->
        println("Answer for part ${index + 1}: ${solutionText(answer)}")
    }
}