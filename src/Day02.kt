enum class Shape (val opponent: Char, val response: Char, val score: Int) {
    Rock('A', 'X', 1),
    Paper('B', 'Y', 2),
    Scissors('C', 'Z', 3);

    private val beats get() = relative(-1)
    private val loosesTo get() = relative(+1)
    private fun relative(delta:Int) = Shape.values()[(this.ordinal + delta + size) % size]

    fun fight(other: Shape): Outcome = when (other) {
        this -> Outcome.Draw
        this.beats -> Outcome.Won
        else -> Outcome.Lost
    }

    fun byOutcome(outcome: Outcome) = when(outcome) {
        Outcome.Lost -> this.beats
        Outcome.Draw -> this
        Outcome.Won -> this.loosesTo
    }

    companion object {
        fun byOpponent(char: Char) = Shape.values().find { it.opponent == char }!!
        fun byResponse(char: Char) = Shape.values().find { it.response == char }!!

        val size get() = Shape.values().size
    }
}

enum class Outcome (val code: Char, val score: Int) {
    Lost('X', 0),
    Draw('Y', 3),
    Won('Z', 6);

    companion object {
        fun byCode(code: Char) = Outcome.values().find { it.code == code }!!
    }
}

class Strategy(val opponent: Shape, val response: Shape) {
    val outcome: Outcome = response.fight(opponent)
    val score: Int = outcome.score + response.score

    companion object {
        fun byResponse(string: String): Strategy = Strategy(
            Shape.byOpponent(string[0]),
            Shape.byResponse(string[2])
        )

        fun byOutcome(string: String): Strategy {
            val opponent = Shape.byOpponent(string[0])
            val outcome = Outcome.byCode(string[2])
            val response = opponent.byOutcome(outcome)

            return Strategy(opponent, response)
        }
    }
}

fun main() {
    fun Sequence<Strategy>.score() = this.map(Strategy::score)

    fun part1(input: Sequence<String>) = input.map(Strategy.Companion::byResponse).score().toList()
    fun part2(input: Sequence<String>) = input.map(Strategy.Companion::byOutcome).score().toList()

    // test if implementation meets criteria from the description, like:
    val testOutput = useSanitizedInput("Day02_test", ::part1)
    check(testOutput == listOf(8, 1, 6))
    check(testOutput.sum() == 15)

    val testOutput2 = useSanitizedInput("Day02_test", ::part2)
    check(testOutput2 == listOf(4, 1, 7))
    check(testOutput2.sum() == 12)

    val output = useSanitizedInput("Day02", ::part1)
    println(output.sum())

    val output2 = useSanitizedInput("Day02", ::part2)
    println(output2.sum())
}