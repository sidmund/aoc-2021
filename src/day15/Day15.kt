package day15

import measure
import readTo2DArray
import showtime
import kotlin.math.abs

private const val DAY = 15

data class Point(val x: Int, val y: Int)

fun neighbors(x: Int, y: Int, width: Int, height: Int, tileFactor: Int): List<Point> = buildList {
    if (x - 1 >= 0)
        add(Point(x - 1, y))
    if (x + 1 < width * tileFactor)
        add(Point(x + 1, y))
    if (y - 1 >= 0)
        add(Point(x, y - 1))
    if (y + 1 < height * tileFactor)
        add(Point(x, y + 1))
}

fun manhattan(cur: Point, goal: Point): Int {
    return abs(goal.x - cur.x) + abs(goal.y - goal.y)
}

fun dijkstra(map: Array<Array<Int>>, width: Int, height: Int, tileFactor: Int): Int {
    val start = Point(0, 0)
    val goal = Point(width * tileFactor - 1, height * tileFactor - 1)

    val frontier = mutableListOf<Pair<Int, Point>>()
    val origin = mutableMapOf<Point, Point>()
    val costs = mutableMapOf<Point, Int>()

    frontier.add(0 to start)
    while (frontier.isNotEmpty()) {
        frontier.sortByDescending { it.first }
        val cur = frontier.removeLast().second
        if (cur == goal) {
            break
        }

        for (next in neighbors(cur.x, cur.y, width, height, tileFactor)) {
            val nextCost = map[next.y % width][next.x % width] + next.x / width + next.y / height
            val cost = (costs[cur] ?: 0) + (if (nextCost > 9) (nextCost + 1) % 10 else nextCost)
            if (next !in costs || cost < costs[next]!!) {
                frontier.add(cost to next)
                origin[next] = cur
                costs[next] = cost
            }
        }
    }

    return costs[goal]!!
}

fun main() {
    val files = setOf(
        Triple("test", 40, 315),
        Triple("input", 583, 2927)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val input = readTo2DArray("day$DAY/$file")

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            dijkstra(input, input[0].size, input.size, 1)
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            dijkstra(input, input[0].size, input.size, 5)
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
