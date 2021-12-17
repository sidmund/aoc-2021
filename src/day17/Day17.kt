package day17

import measure
import readInput
import showtime
import kotlin.math.*

private const val DAY = 17

data class Target(
    val xRange: IntRange,
    val yRange: IntRange
) {
    companion object {
        private val regex = Regex("""target area: x=([-]*[0-9]+)..([-]*[0-9]+), y=([-]*[0-9]+)..([-]*[0-9]+)""")

        fun parse(line: String): Target? =
            regex.matchEntire(line)
                ?.destructured
                ?.let { (x1, x2, y1, y2) ->
                    Target(
                        x1.toInt()..x2.toInt(),
                        y1.toInt()..y2.toInt()
                    )
                }
    }

    fun hit(x: Int, y: Int): Boolean = x in xRange && y in yRange

    fun overshot(x: Int, y: Int): Boolean = x > xRange.last || y < yRange.first
}

fun func(target: Target, Vx: Int, Vy: Int): Int {
    var (x, y) = 0 to 0
    var (vx, vy) = Vx to Vy
    var max = -1000
    while (true) {
        x += vx
        y += vy

        val ax = when {
            vx > 0 -> -1
            vx < 0 -> 1
            else -> 0
        }
        val ay = -1
        vx += ax
        vy += ay

        if (y > max) {
            max = y
        }

        if (target.hit(x, y)) {
            return max
        } else if (target.overshot(x, y)) {
            return -1000
        }
    }
}


fun main() {
    val files = setOf(
        Triple("test", 45, 112),
        Triple("input", 2775, 1566)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val target = readInput("day$DAY/$file").map(Target::parse)[0]!!

        val maxs = mutableListOf<Triple<Int,Int,Int>>()
        for (Vx in 1..target.xRange.last) {
            for (Vy in target.yRange.first..abs(target.yRange.first)) {
                val max = func(target, Vx, Vy)
                if (max != -1000) {
                    maxs.add(Triple(Vx,Vy,max))
                }
            }
        }

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            val max = maxs.maxByOrNull { it.third }!!
            println("(${max.first}, ${max.second}) = ${max.third}")
            max.third
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            maxs.size
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
