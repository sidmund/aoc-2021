package day05

import readInput

data class Segment(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int
) {
    companion object {
        private val regex = Regex("""(\d+),(\d+) -> (\d+),(\d+)""")

        fun parse(line: String): Segment? =
            regex.matchEntire(line)
                ?.destructured
                ?.let { (x1, y1, x2, y2) ->
                    Segment(
                        x1.toInt(),
                        y1.toInt(),
                        x2.toInt(),
                        y2.toInt()
                    )
                }
    }

    fun points(allowDiagonal: Boolean): List<Pair<Int, Int>> {
        val pts = mutableListOf<Pair<Int,Int>>()
        // traverse the line
        for (x in (if (x1 < x2) x1..x2 else x2..x1)) {
            for (y in (if (y1 < y2) y1..y2 else y2..y1)) {
                if (contains(x, y, allowDiagonal)) {
                    pts.add(Pair(x,y))
                }
            }
        }
        return pts
    }

    fun contains(x: Int, y: Int, allowDiagonal: Boolean): Boolean =
        when {
            // horizontal lines
            y1 == y2 -> y == y1 && (if (x1 > x2) x in x2..x1 else x in x1..x2)
            // vertical lines
            x1 == x2 -> x == x1 && (if (y1 > y2) y in y2..y1 else y in y1..y2)
            // diagonals: any slope is allowed, but input data only has segments of slope +/-1
            allowDiagonal -> {
                val dy = y2 - y1
                val dx = x2 - x1
                ((dy / dx) * (x - x1)) == y - y1
                    && (if (x1 > x2) x in x2..x1 else x in x1..x2)
                    && (if (y1 > y2) y in y2..y1 else y in y1..y2)
            }
            // no diagonals
            else -> false
        }
}

fun main() {
    fun overlaps(allowDiagonal: Boolean): Int {
        val dim = 1000

        val segments = readInput("day05/input").map(Segment::parse)
        val diagram = Array(dim) { IntArray(dim) }

        for (s in segments) {
            for ((x, y) in s!!.points(allowDiagonal)) {
                diagram[y][x]++
            }
        }

        return diagram.flatMap { it.asList() }.fold(0) {sum, element -> sum + (if (element >= 2) 1 else 0)}
    }

    println(overlaps(false))
    println(overlaps(true))
}