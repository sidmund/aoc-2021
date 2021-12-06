package day06

import readInput

fun spawn(p0: List<Int>, days: Int): Long {
    // Keeps track of how many fish have each possible value
    val fish = LongArray(9) {0L}
    for (i in p0) {
        fish[i]++
    }

    // Shift the counts around
    for (d in 1..days) {
        val temp = fish[0]
        fish[0] = fish[1]
        fish[1] = fish[2]
        fish[2] = fish[3]
        fish[3] = fish[4]
        fish[4] = fish[5]
        fish[5] = fish[6]
        // Reset the zero fish
        fish[6] = fish[7] + temp
        fish[7] = fish[8]
        // Spawn new fish
        fish[8] = temp
    }

    return fish.sum()
}

fun main() {
    val test = readInput("day06/test")[0].split(",").map(String::toInt)
    val input = readInput("day06/input")[0].split(",").map(String::toInt)

    check(spawn(test, 18) == 26L)
    check(spawn(test, 80) == 5934L)
    check(spawn(test, 256) == 26984457539L)

    println(spawn(input, 80))
    println(spawn(input, 256))
}