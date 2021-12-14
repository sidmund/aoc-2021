package day10

import measure
import readInput
import showtime

val ERROR = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

val COMPLETE = mapOf(
    '(' to 1,
    '[' to 2,
    '{' to 3,
    '<' to 4
)

fun illegal(lastOpen: Char, actual: Char): Int =
    if ((lastOpen == '(' && actual == ')')
        || (lastOpen == '[' && actual == ']')
        || (lastOpen == '{' && actual == '}')
        || (lastOpen == '<' && actual == '>')
    ) 0 else ERROR[actual] ?: 0

fun corrupt(input: List<String>): Int {
    var score = 0
    for (l in input) {
        val opened = mutableListOf<Char>()
        for (c in l) {
            when (c) {
                '{', '[', '(', '<' -> opened.add(c)
                else -> {
                    // a closing bracket should correspond to the last open bracket
                    val illegal = illegal(opened.last(), c)
                    if (illegal != 0) {
                        score += illegal
                        break
                    } else {
                        opened.removeLast()
                    }
                }
            }
        }
    }
    return score
}

fun isLineCorrupt(line: String): Boolean {
    val opened = mutableListOf<Char>()
    for (c in line) {
        when (c) {
            '{', '[', '(', '<' -> opened.add(c)
            else -> {
                if (illegal(opened.last(), c) != 0) return true
                else opened.removeLast()
            }
        }
    }
    return false
}

fun closeLines(input: List<String>): Long {
    val scores = Array<Long>(input.size) { 0 }
    for ((index, l) in input.withIndex()) {
        val opened = mutableListOf<Char>()
        for (c in l) {
            when (c) {
                '{', '[', '(', '<' -> opened.add(c)
                // Can assume matching closing brackets
                else -> opened.removeLast()
            }
        }
        // Should have leftovers in opened:
        scores[index] = opened.reversed().fold(0) { score, c -> (score * 5) + (COMPLETE[c] ?: 0) }
    }
    return scores.sortedArray()[scores.size / 2]
}

fun main() {
    val testInput = readInput("day10/test")
    val input = readInput("day10/input")

    val part1 = measure({ print("[ ${showtime(it)} ]") }) {
        check(corrupt(testInput) == 26397)
        corrupt(input) // 166191
    }
    println(" Answer: $part1")

    val part2 = measure({ print("[ ${showtime(it)} ]") }) {
        check(closeLines(testInput.filter { !isLineCorrupt(it) }) == 288957L)
        closeLines(input.filter { !isLineCorrupt(it) }) // 1152088313
    }
    println(" Answer: $part2")
}
