package day02

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var pos = 0
        var depth = 0

        val commands = input.map { it.split(" ") }.map { it[0] to it[1].toInt() }
        for ((cmd, x) in commands) {
            when (cmd) {
                "forward" -> pos += x
                "down" -> depth += x
                "up" -> depth -= x
            }
        }

        return pos * depth
    }

    fun part2(input: List<String>): Int {
        var pos = 0
        var depth = 0
        var aim = 0

        val commands = input.map { it.split(" ") }.map { it[0] to it[1].toInt() }
        for ((cmd, x) in commands) {
            when (cmd) {
                "forward" -> {
                    pos += x
                    depth += aim * x
                }
                "down" -> aim += x
                "up" -> aim -= x
            }
        }

        return pos * depth
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("dayX/test")
    //check(part1(testInput) == 1)
    //check(part2(testInput) == 1)

    val input = readInput("day02/input")
    println(part1(input))
    println(part2(input))
}
