import kotlin.streams.asSequence

fun main() {
    val startOfPackage = 4
    val startOfMessage = 14

    fun findStart(dataStream: String, noOfChars: Int): Int =
        dataStream.chars()
            .asSequence()
            .withIndex()
            .windowed(noOfChars)
            .first {
                it
                    .map(IndexedValue<Int>::value)
                    .toSet()
                    .size == noOfChars
            }
            .last()
            .index + 1

    check(findStart("mjqjpqmgbljsphdztnvjfqwrcgsmlb", startOfPackage) == 7)
    check(findStart("bvwbjplbgvbhsrlpgdmjqwftvncz", startOfPackage) == 5)
    check(findStart("nppdvjthqldpwncqszvftbrmjlhg", startOfPackage) == 6)
    check(findStart("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", startOfPackage) == 10)
    check(findStart("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", startOfPackage) == 11)

    val input = readInput("Day06").first()
    println(findStart(input, startOfPackage))

    check(findStart("mjqjpqmgbljsphdztnvjfqwrcgsmlb", startOfMessage) == 19)
    check(findStart("bvwbjplbgvbhsrlpgdmjqwftvncz", startOfMessage) == 23)
    check(findStart("nppdvjthqldpwncqszvftbrmjlhg", startOfMessage) == 23)
    check(findStart("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", startOfMessage) == 29)
    check(findStart("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", startOfMessage) == 26)

    println(findStart(input, startOfMessage))
}
