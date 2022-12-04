fun main() {

    fun cleaningRanges(input: Sequence<String>) =
            input
                    .map {
                        it.split(',').map { elfRange ->
                            val (left, right) = elfRange.split('-').map(String::toInt)
                            left..right
                        }
                    }

    fun part1(input: Sequence<String>) = cleaningRanges(input)
            .count { (left, right) ->
                (left.contains(right.first) && left.contains(right.last)) ||
                        (right.contains(left.first) && right.contains(left.last))
            }

    fun part2(input: Sequence<String>) = cleaningRanges(input)
            .count { (left, right) ->
                left.contains(right.first) || left.contains(right.last) ||
                        right.contains(left.first) || right.contains(left.last)
            }

    // test if implementation meets criteria from the description, like:
    val testOutput = useSanitizedInput("Day04_test", ::part1)
    check(testOutput == 2)
    val testOutput2 = useSanitizedInput("Day04_test", ::part2)
    check(testOutput2 == 4)

    val output = useSanitizedInput("Day04", ::part1)
    println(output)
    val output2 = useSanitizedInput("Day04", ::part2)
    println(output2)
}
