package day09

import measure
import readInput
import showtime

fun neighbors(x: Int, y: Int, width: Int, length: Int): List<Pair<Int, Int>> =
    when {
        x == 0 && y == 0 -> {
            // left top corner
            listOf(Pair(x, y + 1), Pair(x + 1, y))
        }
        x == 0 && y == length - 1 -> {
            // left bottom corner
            listOf(Pair(x, y - 1), Pair(x + 1, y))
        }
        x == 0 -> {
            // left edge
            listOf(Pair(x, y - 1), Pair(x + 1, y), Pair(x, y + 1))
        }
        x == width - 1 && y == 0 -> {
            // right top corner
            listOf(Pair(x, y + 1), Pair(x - 1, y))
        }
        x == width - 1 && y == length - 1 -> {
            // right bottom corner
            listOf(Pair(x, y - 1), Pair(x - 1, y))
        }
        x == width - 1 -> {
            // right edge
            listOf(Pair(x, y + 1), Pair(x - 1, y), Pair(x, y - 1))
        }
        y == 0 -> {
            // top edge
            listOf(Pair(x, y + 1), Pair(x - 1, y), Pair(x + 1, y))
        }
        y == length - 1 -> {
            // btm edge
            listOf(Pair(x, y - 1), Pair(x - 1, y), Pair(x + 1, y))
        }
        else -> {
            // non-edge/corner points
            listOf(Pair(x, y + 1), Pair(x, y - 1), Pair(x - 1, y), Pair(x + 1, y))
        }
    }

fun isLowPoint(x: Int, y: Int, width: Int, length: Int, heightmap: Array<Array<Int>>): Boolean {
    val neighbors = neighbors(x, y, width, length)
    return neighbors.count { heightmap[y][x] < heightmap[it.second][it.first] } == neighbors.size
}

fun risk(heightmap: Array<Array<Int>>): Int {
    val length = heightmap.size
    val width = heightmap[0].size

    return (0 until length).fold(0) { total, y ->
        total + (0 until width).fold(0) { sum, x ->
            sum + (if (isLowPoint(x, y, width, length, heightmap)) heightmap[y][x] + 1 else 0)
        }
    }
}

fun basinSize(heightmap: Array<Array<Int>>): Int {
    val length = heightmap.size
    val width = heightmap[0].size

    val sizes = mutableListOf<Int>()
    for (y in 0 until length) {
        for (x in 0 until width) {
            if (isLowPoint(x, y, width, length, heightmap)) {
                sizes.add(basin(x, y, width, length, heightmap, mutableSetOf()))
            }
        }
    }
    sizes.sortDescending()
    return sizes.take(3).reduce { mult, size -> mult * size }
}

fun basin(x: Int, y: Int, width: Int, length: Int, heightmap: Array<Array<Int>>, basinPts: MutableSet<Pair<Int, Int>>): Int {
    if (x to y !in basinPts) {
        basinPts.add(Pair(x,y))
    }
    // Filter out the 9's and points that are already contained in the basin
    (neighbors(x, y, width, length).filter { heightmap[it.second][it.first] != 9 } - basinPts)
            // Go through all its neighbors and determine their neighbors
        .map { n -> basin(n.first, n.second, width, length, heightmap, basinPts) }

    return basinPts.size
}

fun main() {
    val testHeightmap = readInput("day09/test")
        .map { it.toCharArray().map(Char::digitToInt).toTypedArray()}
        .toTypedArray()
    val heightmap = readInput("day09/input")
        .map { it.toCharArray().map(Char::digitToInt).toTypedArray() }
        .toTypedArray()

    val part1 = measure({ print("[ ${showtime(it)} ]") }) {
        check(risk(testHeightmap) == 15)
        risk(heightmap) // 532
    }
    println(" Answer: $part1")

    val part2 = measure({ print("[ ${showtime(it)} ]") }) {
        check(basinSize(testHeightmap) == 1134)
        basinSize(heightmap) // 1110780
    }
    println(" Answer: $part2")
}
