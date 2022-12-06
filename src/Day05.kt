import Crate.Companion.chars
import Crate.Companion.print
import kotlin.time.measureTime

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
            //description.chars()
            val movement = pattern.matchEntire(description)!!
            return Movement(movement.value("count"), movement.value("from"), movement.value("to"))
        }
    }
}

// class FastList<T>(private val items: Array)

class SupplyStacks {

    private val stacks: Array<Array<Crate?>>
    private val stackSize: IntArray

    private constructor(stacks: Array<List<Crate>>) {
        val capacity = stacks.sumOf { it.size }
        this.stacks = Array(stacks.size) { s ->
            val stack = stacks[s]
            Array(capacity) { i ->
                if (i < stack.size) stack[i] else null
            }
        }
        this.stackSize = stacks.map { it.size }.toIntArray()
    }

    enum class MoveStyle { OneByOne, Stack }

    private fun moveCrates(count: Int, from: Int, to:Int) {
        val source = stacks[from]
        val target = stacks[to]

        val s = stackSize[from]-count
        val t = stackSize[to]

        for (i in 0..<count) {
            target[t+i] = source[s+i]
        }

        stackSize[from] -= count
        stackSize[to] += count
    }

    fun moveCrates(iter: Iterator<String>, moveStyle: MoveStyle) = iter.forEachRemaining {
        val m = Movement.parse(it)

        when (moveStyle) {
            MoveStyle.OneByOne -> for (i in 0..<m.count) moveCrates(1, m.from-1, m.to-1)
            MoveStyle.Stack -> moveCrates(m.count, m.from-1, m.to-1)
        }
    }
/*
    private fun printStack() {
        val maxHeight = stacks.maxOf { it.size }
        for (i in (maxHeight - 1).downTo(0)) {
            stacks.map { if (i < it.size) it[i] else Crate.empty }.print()
        }
        println()
    }
 */

    val size get() = stackSize.sum()

    val topItems get() = stacks.mapIndexed{ s, crates -> crates[stackSize[s] - 1] ?: Crate.empty}

    companion object {
        fun parse(iterator: Iterator<String>): SupplyStacks {
            val lines = mutableListOf<CharArray>()

            while (iterator.hasNext()) {
                val current = iterator.next()
                if (current.isNullOrBlank()) break

                val line = current.chunked(4) { it[1] }
                if (line.any { it.isDigit() }) continue

                lines.add(line.toCharArray())
            }

            while (stacks.size < line.size) stacks.add(CharArray())

            line.mapIndexed { index, c ->
                if (c.isLetter()) stacks[index].add(Crate(c))
            }


            stacks.forEach {
                it.reverse()
            }

            return SupplyStacks(stacks.toTypedArray())
        }
    }
}

fun main() {
    fun part1(input: Sequence<String>): String {
        val iter = input.iterator()
        val ss = SupplyStacks.parse(iter)
        ss.moveCrates(iter, SupplyStacks.MoveStyle.OneByOne)

        return ss.topItems.chars
    }

    fun part2(input: Sequence<String>): String {
        val iter = input.iterator()
        val ss = SupplyStacks.parse(iter)
        ss.moveCrates(iter, SupplyStacks.MoveStyle.Stack)

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
