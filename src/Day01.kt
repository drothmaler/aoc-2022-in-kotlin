fun main() {
    fun calorieCounting(input: Sequence<String>): Sequence<Int> =
        input
            .map { it.toIntOrNull() }
            .splitByNull()
            .map { it.sum() }

    fun top3(input: Sequence<String>) : List<Int> = calorieCounting(input).sortedDescending().take(3).toList()

    // test if implementation meets criteria from the description, like:
    val testOutput = useSanitizedInput("Day01_test", ::top3)
    check(testOutput == listOf(24000, 11000, 10000))
    check(testOutput.max() == 24000)
    check(testOutput.sum() == 45000)

    val output = useSanitizedInput("Day01", ::top3)
    println(output.max())
    println(output.sum())
}
