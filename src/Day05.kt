import Crate.Companion.chars
import Crate.Companion.print
import kotlin.time.measureTime

var debug = false

@JvmInline
value class Crate(val char:Char) {
    override fun toString() = if (char == ' ') "   " else "[$char]"

    companion object {
        val empty = Crate(' ')

        val List<Crate>.chars get() = String(this.map { it.char }.toCharArray())

        fun List<Crate>.print() = println(this.joinToString(" ") )
    }
}

data class Movement(val count:Int, val from:Int, val to:Int) {

    override fun toString(): String {
        return "move $count from $from to $to"
    }

    companion object {
        private val pattern =  Regex("move (?<count>\\d+) from (?<from>\\d+) to (?<to>\\d+)")

        private fun MatchResult.value(name: String) = groups[name]!!.value.toInt()

        fun parse(description: String): Movement {
            val movement = pattern.matchEntire(description)!!
            return Movement(movement.value("count"), movement.value("from"), movement.value("to"))
        }
    }
}

class SupplyStacks(private val stacks: Array<ArrayDeque<Crate>>) {
    fun moveCrates(iter: Iterator<String>) {
        iter.forEachRemaining {
            val m = Movement.parse(it)

            for (i in 0..<m.count) {
                stacks[m.to-1].addLast(stacks[m.from-1].removeLast())
            }
        }
    }

    fun moveCrates2(iter: Iterator<String>) {
        val craneStack = ArrayDeque<Crate>(50)
        iter.forEachRemaining {
            val m = Movement.parse(it)

            for (i in 0..<m.count) {
                craneStack.addLast(stacks[m.from-1].removeLast())
            }
            for (i in 0..<m.count) {
                stacks[m.to-1].addLast(craneStack.removeLast())
            }
        }
    }

    private fun printStack() {
        val maxHeight = stacks.maxOf { it.size }
        for (i in (maxHeight - 1).downTo(0)) {
            stacks.map { if (i < it.size) it[i] else Crate.empty }.print()
        }
        println()
    }

    val size get() = stacks.sumOf { it.size }

    val topItems get() = stacks.map { it.lastOrNull() ?: Crate.empty }

    companion object {
        fun parse(iterator: Iterator<String>): SupplyStacks {
            val stacks = mutableListOf<MutableList<Crate>>()

            while (iterator.hasNext()) {
                val current = iterator.next()
                val line = current.chunked(4) { it[1] }
                if (line.any { it.isDigit() }) continue
                if (line.isEmpty()) break

                while (stacks.size < line.size) stacks.add(ArrayDeque())

                line.mapIndexed { index, c ->
                    if (c.isLetter()) stacks[index].add(Crate(c))
                }
            }
            val size = stacks.sumOf { it.size }

            return SupplyStacks(
                stacks.map {
                    val stack = ArrayDeque<Crate>(size)
                    stack.addAll(it.asReversed())
                    stack
                }.toTypedArray()
            )
        }
    }
}

fun main() {
    fun part1(input: Sequence<String>): String {
        val iter = input.iterator()
        val ss = SupplyStacks.parse(iter)
        ss.moveCrates(iter)

        return ss.topItems.chars
    }

    fun part2(input: Sequence<String>): String {
        val iter = input.iterator()
        val ss = SupplyStacks.parse(iter)
        ss.moveCrates2(iter)

        return ss.topItems.chars
    }

    // test if implementation meets criteria from the description, like:
    println("took " + measureTime {
        val testOutput = useSanitizedInput("Day05_test", ::part1)
        println(testOutput)
        check(testOutput == "CMZ")
    })
    println("took " + measureTime {
        val testOutput = useSanitizedInput("Day05_test", ::part2)
        println(testOutput)
        check(testOutput == "MCD")
    })

    println("took " + measureTime {
        val largeOutput = useSanitizedInput("aoc_2022_day05_large_input", ::part1)
        println(largeOutput)
    })

    // readln()

    //debug = true

    println("took " + measureTime {
        val output = useSanitizedInput("Day05", ::part1)
        println(output)
    })
    println("took " + measureTime {
        val output = useSanitizedInput("Day05", ::part2)
        println(output)
    })
}
