package day11

import measure
import readTo2DArray
import showtime

// Including diagonals
fun neighbors(x: Int, y: Int, width: Int, length: Int): List<Pair<Int, Int>> =
    when {
        x == 0 && y == 0 -> {
            // left top corner
            listOf(Pair(x, y + 1), Pair(x + 1, y), Pair(x + 1, y + 1))
        }
        x == 0 && y == length - 1 -> {
            // left bottom corner
            listOf(Pair(x, y - 1), Pair(x + 1, y), Pair(x + 1, y - 1))
        }
        x == 0 -> {
            // left edge
            listOf(Pair(x, y - 1), Pair(x + 1, y), Pair(x, y + 1), Pair(x + 1, y - 1), Pair(x + 1, y + 1))
        }
        x == width - 1 && y == 0 -> {
            // right top corner
            listOf(Pair(x, y + 1), Pair(x - 1, y), Pair(x - 1, y + 1))
        }
        x == width - 1 && y == length - 1 -> {
            // right bottom corner
            listOf(Pair(x, y - 1), Pair(x - 1, y), Pair(x - 1, y - 1))
        }
        x == width - 1 -> {
            // right edge
            listOf(Pair(x, y + 1), Pair(x - 1, y), Pair(x, y - 1), Pair(x - 1, y - 1), Pair(x - 1, y + 1))
        }
        y == 0 -> {
            // top edge
            listOf(Pair(x, y + 1), Pair(x - 1, y), Pair(x + 1, y), Pair(x - 1, y + 1), Pair(x + 1, y + 1))
        }
        y == length - 1 -> {
            // btm edge
            listOf(Pair(x, y - 1), Pair(x - 1, y), Pair(x + 1, y), Pair(x - 1, y - 1), Pair(x + 1, y - 1))
        }
        else -> {
            // non-edge/corner points
            listOf(
                Pair(x, y + 1), Pair(x, y - 1), Pair(x - 1, y), Pair(x + 1, y),
                Pair(x - 1, y - 1), Pair(x - 1, y + 1), Pair(x + 1, y - 1), Pair(x + 1, y + 1)
            )
        }
    }

var flashes = 0

fun spread(x: Int, y: Int, width: Int, length: Int, levels: Array<Array<Int>>) {
    // Go through all neighbors (except those that flash already) and increase their energy
    val neighbors = neighbors(x, y, width, length).filter { levels[it.second][it.first] > 0 }
    val flashing = mutableSetOf<Pair<Int, Int>>()
    for (octopus in neighbors) {
        levels[octopus.second][octopus.first]++
        if (levels[octopus.second][octopus.first] > 9) {
            levels[octopus.second][octopus.first] = 0
            flashing.add(octopus)
        }
    }
    flashes += flashing.size
    for (octopus in flashing) {
        spread(octopus.first, octopus.second, width, length, levels)
    }
}

fun showParty(width: Int, length: Int, levels: Array<Array<Int>>, msg: String) {
    println(msg)
    for (y in 0 until length) {
        for (x in 0 until width) {
            print(levels[y][x])
        }
        println()
    }
    println()
}

fun synchronized(width: Int, length: Int, levels: Array<Array<Int>>): Boolean {
    for (y in 0 until length) {
        for (x in 0 until width) {
            if (levels[y][x] != 0) {
                return false
            }
        }
    }
    return true
}

fun octoparty(levels: Array<Array<Int>>, steps: Int, verbose: Boolean): Int {
    val length = levels.size
    val width = levels[0].size

    if (verbose) showParty(width, length, levels, "Before any steps:")

    for (step in 1..steps) {
        val flashing = mutableSetOf<Pair<Int, Int>>()
        for (y in 0 until length) {
            for (x in 0 until width) {
                levels[y][x]++
                if (levels[y][x] > 9) {
                    levels[y][x] = 0
                    flashing.add(x to y)
                }
            }
        }
        flashes += flashing.size
        for (octopus in flashing) {
            // Spread the party
            spread(octopus.first, octopus.second, width, length, levels)
        }

        if (verbose && step % 10 == 0) showParty(width, length, levels, "After step $step:")

        if (synchronized(width, length, levels)) {
            if (verbose) showParty(width, length, levels, "After step $step:")
            return step
        }
    }

    return flashes
}

fun main() {
    val part1 = measure({ print("[ ${showtime(it)} ]") }) {
        val testLevels = readTo2DArray("day11/test")
        val levels = readTo2DArray("day11/input")

        check(octoparty(testLevels, 100, false) == 1656)
        flashes = 0
        octoparty(levels, 100, false) // 1644
    }
    println(" Answer: $part1")

    val part2 = measure({ print("[ ${showtime(it)} ]") }) {
        val testLevels = readTo2DArray("day11/test")
        val levels = readTo2DArray("day11/input")

        check(octoparty(testLevels, 200, false) == 195)
        octoparty(levels, 500, false) // 229
    }
    println(" Answer: $part2")
}