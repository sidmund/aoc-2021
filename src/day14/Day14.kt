package day14

import measure
import readInput
import showtime
import kotlin.math.ceil

private const val DAY = 14

fun polymerize(rules: Map<String, Char>, template: Map<String, Long>, step: Int): Map<String, Long> {
    var paircount = template
    repeat(step) {
        val newpaircount = mutableMapOf<String, Long>()
        paircount.forEach { (pair, count) ->
            val ins = rules[pair]!!
            val p1 = "${pair[0]}$ins"
            val p2 = "$ins${pair[1]}"
            newpaircount[p1] = count + (newpaircount[p1] ?: 0)
            newpaircount[p2] = count + (newpaircount[p2] ?: 0)
        }
        paircount = newpaircount
    }
    return paircount
}

fun quantity(polymer: Map<String, Long>): Long {
    val freq = Array(26) { 0L }
    polymer.forEach { (pair, count) -> pair.forEach { freq[it.code - 65] += count } }

    return ceil((freq.maxByOrNull { it }!! - freq.filter { it > 0 }.minByOrNull { it }!!) / 2.0).toLong()
}

fun main() {
    val files = setOf(
        Triple("test", 1588L, 2188189693529L),
        Triple("input", 3411L, 7477815755570L)
    )

    files.forEach { (file, answer1, answer2) ->
        println("=== $file ===")
        val input = readInput("day$DAY/$file")

        val rules = input.drop(2).map { it.split(" -> ") }.associate { it[0] to it[1][0] }
        val template = mutableMapOf<String, Long>()
        for (pair in input[0].windowed(2)) {
            template[pair] = 1 + (template[pair] ?: 0)
        }

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            quantity(polymerize(rules, template, 10))
        }
        println(" Answer: $part1 (${if (answer1 == part1) "correct" else "wrong"})")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            quantity(polymerize(rules, template, 40))
        }
        println(" Answer: $part2 (${if (answer2 == part2) "correct" else "wrong"})\n")
    }
}
