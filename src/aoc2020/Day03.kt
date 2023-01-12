package aoc2020

import readInput

fun findTrees(input: List<String>, vector: Pair<Int, Int>): Int {
    val width = input[0].length
    val (dx, dy) = vector
    var x = 0
    var trees = 0

    for (row in input.indices step dy) {
        if (input[row][x] == '#') {
            trees++
        }
        x += dx
        x %= width
    }

    val treez = input.indices.count { y ->
        y % dy == 0 && input[y][y / dy * dx % width] == '#'
    }
    return treez
}

fun main() {
    val test = readInput("aoc2020/day03test")
    val input = readInput("aoc2020/day03")

    val vectors = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)

    // Part 1
    println(findTrees(input, 3 to 1))

    // Part 2
    println((0..4).map { findTrees(input, vectors[it]) }.fold(1) { mult, tree -> mult * tree })
    // .reduce{a,b -> a*b}
}