package day24

import measure
import readInput
import showtime

private const val DAY = 24

data class Data(
    val x: Int
) {
    companion object {
        private val regex = Regex("""(\d+): ([a-z])""")

        fun parse(line: String): Data? =
            regex.matchEntire(line)
                ?.destructured
                ?.let { (x) ->
                    Data(
                        x.toInt()
                    )
                }
    }
}

fun main() {
    val files = setOf(
        Triple("test", 0, 0),
        Triple("input", 0, 0)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val input = readInput("day$DAY/$file")

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            0
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            0
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
