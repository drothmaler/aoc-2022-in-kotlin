import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun <T> useInput(name: String, block: (Sequence<String>) -> T) = File("src", "$name.txt")
    .useLines(block = block)

fun <T> useSanitizedInput(name: String, block: (Sequence<String>) -> T) = useInput(name) {
    block(it.map(String::trim))
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


fun <T> Sequence<T>.splitByNull() =
    splitIf { it == null }
    .map { it.filterNotNull() }

fun <T> Sequence<T>.splitIf(isSplitter: (T) -> Boolean) = sequence {
    val iter = iterator()

    while (iter.hasNext()) {
        yield(iter.takeUntil(isSplitter).asSequence())
    }
}

fun <T> Iterator<T>.takeUntil(isSplitter: (T) -> Boolean) = iterator {
    val parent = this@takeUntil

    while (parent.hasNext()) {
        val item = parent.next();

        if (isSplitter(item)) {
            return@iterator
        }

        yield(item)
    }
}
