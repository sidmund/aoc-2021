package day13

import measure
import readInput
import kotlin.math.max
import kotlin.math.min

private const val DAY = 13

fun show(paper: Array<Array<Boolean>>, width: Int, height: Int) {
    for (y in 1..height) {
        for (x in 1..width) {
            print(if (paper[y - 1][x - 1]) '#' else '.')
        }
        println()
    }
}

fun foldX(paper: Array<Array<Boolean>>, width: Int, height: Int, line: Int): Array<Array<Boolean>> {
    val left = Array(height) { Array(line) { false } }
    for (y in 0 until height) {
        for (x in 0 until line) {
            left[y][x] = paper[y][x]
        }
    }
    // Extract right part, and immediately flip it
    val right = Array(height) { Array(width - line - 1) { false } }
    for (y in 0 until height) {
        for (x in 0 until (width - line - 1)) {
            right[y][x] = paper[y][width - 1 - x]
        }
    }
    // Overlay them
    val (min, max) = min(line, width - line - 1) to max(line, width - line - 1)
    val leftShorter = min <= max
    val overlay = Array(height) { Array(max) { false } }
    for (y in 0 until height) {
        for (x in 0 until max) {
            if (leftShorter) {
                if (left[y][x]) overlay[y][x] = true
            } else {
                if (right[y][x]) overlay[y][x] = true
            }
        }
    }
    for (y in 0 until height) {
        for (x in 0 until min) {
            if (!leftShorter) {
                if (left[y][x]) {
                    overlay[y][max-min+x] = true
                }
            } else {
                if (right[y][x]) {
                    overlay[y][max-min+x] = true
                }
            }
        }
    }
    return overlay
}

fun foldY(paper: Array<Array<Boolean>>, width: Int, height: Int, line: Int): Array<Array<Boolean>> {
    val top = Array(line) { Array(width) { false } }
    for (y in 0 until line) {
        for (x in 0 until width) {
            top[y][x] = paper[y][x]
        }
    }
    // Extract bottom part, and immediately flip it
    val btm = Array(height - line - 1) { Array(width) { false } }
    for (y in 0 until (height - line - 1)) {
        for (x in 0 until width) {
            btm[y][x] = paper[height - 1 - y][x]
        }
    }
    // Overlay them
    val (min, max) = min(line, height - line - 1) to max(line, height - line - 1)
    val topShorter = min <= max
    val overlay = Array(max) { Array(width) { false } }
    for (y in 0 until max) {
        for (x in 0 until width) {
            if (topShorter) {
                if (top[y][x]) overlay[y][x] = true
            } else {
                if (btm[y][x]) overlay[y][x] = true
            }
        }
    }
    for (y in 0 until min) {
        for (x in 0 until width) {
            if (!topShorter) {
                if (top[y][x]) {
                    overlay[max-min+y][x] = true
                }
            } else {
                if (btm[y][x]) {
                    overlay[max-min+y][x] = true
                }
            }
        }
    }
    return overlay
}

fun count(paper: Array<Array<Boolean>>): Int =
    paper.sumOf { row -> row.count { it } }

fun main() {
    // Pair containing (answer part 1, answer part 2)
    val files = mapOf(
        "test" to Pair(17, 16),
        "input" to Pair(716, 97) // "R P C K F B L R"
    )

    files.forEach { (file, answers) ->
        println("=== File: $file ===")
        val input = readInput("day$DAY/$file")

        val coords = input
            .filter { it.contains(",") }
            .map { it.split(",") }
            .map { it[0].toInt() to it[1].toInt() }
        val instructs = input
            .filter { it.contains("fold") }
            .map { it.split(" ")[2].split("=") }
            .map { it[0] to it[1].toInt() }

        var (width, height) = (coords.maxOf { it.first } + 1 to coords.maxOf { it.second } + 1)
        var paper = Array(height) { y -> Array(width) { x -> coords.contains(x to y) } }

        val part1 = measure({ print("[$it ms]") }) {
            val (axis, line) = instructs[0]
                paper = when (axis) {
                    "x" -> foldX(paper, width, height, line)
                    else -> foldY(paper, width, height, line)
                }
                width = paper[0].size
                height = paper.size
            count(paper)
        }
        println(" Answer: $part1 (${if (answers.first == part1) "correct" else "wrong"})")

        val part2 = measure({ print("[$it ms]") }) {
            // Skip first one (already done in Part 1) to ~half the computation time
            for (i in 1 until instructs.size) {
                val (axis, line) = instructs[i]
                paper = when (axis) {
                    "x" -> foldX(paper, width, height, line)
                    else -> foldY(paper, width, height, line)
                }
                width = paper[0].size
                height = paper.size
            }
            count(paper)
        }
        println(" Answer: $part2 (${if (answers.second == part2) "correct" else "wrong"})\n")

        if (file == "input") {
            show(paper, width, height)
        }
    }
}
