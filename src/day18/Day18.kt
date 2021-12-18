package day18

import measure
import readInput
import showtime

private const val DAY = 18

fun main() {
    testExplode()
    testSplit()
    testAddition()
    testMagnitude()

    val files = setOf(
        Triple("test", 4140, 3993),
        Triple("input", 3675, 4650)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val snailfish = readInput("day$DAY/$file").map(Snailfish::convert)

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            snailfish.reduce { snail, fish -> snail snail fish }.magnitude()
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            snailfish.map { snail ->
                snailfish.map { fish ->
                    (snail snail fish).magnitude()
                }.reduce { max, magn1 -> if (magn1 > max) magn1 else max }
            }.reduce { max, magn2 -> if (magn2 > max) magn2 else max }
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
