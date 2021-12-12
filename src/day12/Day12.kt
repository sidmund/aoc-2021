package day12

import measure
import readInput

// returns just the path count
fun pathfinder(
    nodes: Map<String, MutableSet<String>>,
    current: String = "start",
    visited: List<String> = listOf(),
    twice: Boolean = false
): Int {

    val newVisited = visited + current
    if (current == "end") {
        return 1
    }

    val neighbors = nodes[current]
        ?.filter { neighbor ->
            neighbor != "start"
                    && !(
                    // Assume: nodes are only fully lowercase or fully uppercase
                    neighbor[0].isLowerCase() && newVisited.contains(neighbor)
                            && (
                            (twice && newVisited.filter { node -> node[0].isLowerCase() }.groupBy { it }
                                .any { entry -> entry.value.size >= 2 })
                                    || (!twice)
                            )
                    )
        } ?: emptyList()
    return neighbors.sumOf { pathfinder(nodes, it, newVisited, twice)}
}

fun main() {
    val files = mapOf(
        "test" to Pair(10, 36),
        "test2" to Pair(19, 103),
        "test3" to Pair(226, 3509),
        "input" to Pair(3463, 91533)
    )

    files.forEach { (file, answers) ->
        println("=== File: $file ===")
        val input = readInput("day12/$file").map { it.split("-") }

        val nodes = input.flatten().toSet().associateWith { mutableSetOf<String>() }
        for (connection in input) {
            val (from, to) = connection[0] to connection[1]
            nodes[from]?.add(to)
            nodes[to]?.add(from)
        }

        val part1 = measure({ println("Part 1 took $it ms") }) {
            pathfinder(nodes, twice = false)
        }
        println(" Answer: $part1 (${if (answers.first == part1) "correct" else "wrong"})")

        val part2 = measure({ println("Part 2 took $it ms") }) {
            pathfinder(nodes, twice = true)
        }
        println(" Answer: $part2 (${if (answers.second == part2) "correct" else "wrong"})")

        println()
    }
}