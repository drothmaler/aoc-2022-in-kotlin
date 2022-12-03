import ElfGroup.Companion.groupBadges
import Rucksack.Companion.groupElves
import Rucksack.Companion.toRucksacks

@JvmInline
value class Item(private val char: Char) {
   val priority: Int
       get() = when (char) {
           in 'a'..'z' -> char.code - 97 + 1
           in 'A'..'Z' -> char.code - 65 + 27
           else -> throw IllegalArgumentException()
       }

    override fun toString(): String = char.toString()
}
data class Compartment(val items:Sequence<Item>) {
    constructor(items: CharSequence) : this(items.asSequence().map { Item(it) })

    override fun toString(): String = items.joinToString("")
}

data class Rucksack (val left: Compartment, val right: Compartment) {
    constructor(items: String) : this(
            Compartment(items.subSequence(0, items.length / 2)),
            Compartment(items.subSequence(items.length / 2, items.length))
    )

    private val sharedItems = left.items.toSet().intersect(right.items.toSet())

    val sharedItem = sharedItems.single()
    val compartments = listOf(left.toString(), right.toString())
    val items = left.items.toSet() + right.items.toSet()

    companion object {
        fun Sequence<String>.toRucksacks() = this.map { Rucksack(it) }
        fun Sequence<Rucksack>.groupElves() = this.chunked(3).map { ElfGroup(it) }
    }
}

@JvmInline
value class ElfGroup(private val elves: List<Rucksack>) {
    val groupBadge get() = elves.map { it.items }.reduce { acc, items -> acc.intersect(items)}.single()

    companion object {
        val Sequence<ElfGroup>.groupBadges get() = this.map { it.groupBadge }
    }
}

fun main() {
    fun part1(input: Sequence<String>) = input.toRucksacks().sumOf { it.sharedItem.priority }
    fun part2(input: Sequence<String>) = input.toRucksacks().groupElves().groupBadges.sumOf { it.priority }

    val testInput = readSanitizedInput("Day03_test")
    val testOutput = testInput.asSequence().toRucksacks().toList()
    val testCompartments = testOutput.take(3).map { it.compartments }
    val expectedCompartments = listOf(
            listOf("vJrwpWtwJgWr", "hcsFMMfFFhFp"),
            listOf("jqHRNqRjqzjGDLGL", "rsFMfFZSrLrFZsSL"),
            listOf("PmmdzqPrV", "vPwwTWBwg")
    )
    check(testCompartments.count() == expectedCompartments.count())
    testCompartments.zip(expectedCompartments).forEach { (actual, expected) ->
        check(actual == expected)
    }

    val testShared = testOutput.map { it.sharedItem.toString() }
    check(testShared == listOf(
            "p", "L", "P", "v", "t", "s"
    ))

    val test1Output = useSanitizedInput("Day03_test", ::part1)
    check(test1Output == 157)

    val testBadges = testOutput.asSequence().groupElves().groupBadges.map { it.toString() }.toList()
    check(testBadges == listOf(
            "r", "Z"
    ))

    val test2Output = useSanitizedInput("Day03_test", ::part2)
    check(test2Output == 70)

    val output = useSanitizedInput("Day03", ::part1)
    println(output)

    val output2 = useSanitizedInput("Day03", ::part2)
    println(output2)
}


