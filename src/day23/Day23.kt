package day23

import measure
import readInput
import showtime
import kotlin.math.abs

private const val DAY = 23

data class Pos2D(val x: Int, val y: Int) {
    fun neighbors(): List<Pos2D> = buildList {
        add(Pos2D(x - 1, y))
        add(Pos2D(x + 1, y))
        add(Pos2D(x, y - 1))
        add(Pos2D(x, y + 1))
    }

    fun manhattan(other: Pos2D): Int {
        return abs(this.x - other.x) + abs(this.y - other.y)
    }
}

enum class Type(val energy: Int) {
    A(1), B(10), C(100), D(1000)
}

data class Amphipod(
    val type: Type,
    val pos: Pos2D,
    val target: Room
) {
    fun isHome(): Boolean = target.contains(pos)
    fun energy(): Int = type.energy
}

data class Room(
    // top left origin point
    val p: Pos2D,
    val w: Int,
    val h: Int
) {
    fun contains(pos: Pos2D): Boolean =
        pos.x >= p.x && pos.x < p.x + w && pos.y >= p.y && pos.y < p.y + h

    fun positions(): List<Pos2D> =
        buildList {
            for (y in p.y until p.y + h) {
                for (x in p.x until p.x + w) {
                    add(Pos2D(x, y))
                }
            }
        }

    fun capacity(): Int = w * h
}

class Burrow(
    // 'true' for a wall, 'false' for an open space
    val walls: Array<Array<Boolean>>,
    val rooms: List<Room>,
    val hallway: Room,
    val w: Int,
    val h: Int,
    val pods: List<Amphipod>
) {
    private val entrances =
        setOf(Pos2D(3, 1), Pos2D(5, 1), Pos2D(7, 1), Pos2D(9, 1))

    private fun allAmphipodsAreHome(): Boolean =
        pods.all { it.isHome() }

    fun print() {
        for (y in 0 until h) {
            for (x in 0 until w) {
                val pod = isOccupied(Pos2D(x, y))
                if (pod != null) {
                    print(pod.type)
                } else {
                    print(if (walls[y][x]) '#' else '.')
                }
            }
            println()
        }
        println()
    }

    // find the spots in the hallway that are (a) not an entrance, and (b) not occupied, and that are (c) not blocked
    // aka: ..C.++B.
    //      ###A####
    // @param x  the x position of the amphipod that wants to move into the hallway
    private fun hallwayRange(x: Int): List<Pos2D> {
        val positions = hallway.positions().sortedBy { it.x }
        val podsInHallway = pods.map { it.pos }.filter { hallway.contains(it) }

        return if (podsInHallway.isNotEmpty()) {
            val leftBlock = podsInHallway.findLast { it.x < x } ?: Pos2D(-1, 0)
            val rightBlock = podsInHallway.find { it.x > x } ?: Pos2D(positions.size, 0)

            positions.takeWhile { it.x > leftBlock.x && it.x < rightBlock.x }
        } else {
            positions
        }
    }

    // construct a list of positions the supplied amphipod can move to
    // for further cost calculation
    fun possibleLocations(amphipod: Amphipod): List<Pos2D> =
        buildList {
            // Find the room this amphipod is in
            val room = rooms.find { room -> room.contains(amphipod.pos) } ?: hallway
            if (room != hallway) {
                // pod is in a room: it can stay or enter the hallway at any free spot (not above entrance)
                // add option for staying
                add(amphipod.pos)

                // add option for each pos in hallway (restricted by occupied spots) and not entrances
                addAll(hallwayRange(amphipod.pos.x) - entrances)
            } else {
                // pod is in hallway: it can stay, or move into its home room
                // add option for staying in hallway
                add(amphipod.pos)

                // add option for moving into home (target) room (if empty or contains others of its kind)
                val homePositions = amphipod.target.positions()
                val podsInRoom = pods.filter { homePositions.contains(it.pos) && it != amphipod }
                if (podsInRoom.size < amphipod.target.capacity() && podsInRoom.all { it.type == amphipod.type }) {
                    if (podsInRoom.isEmpty()) {
                        // all spaces in room are empty,
                        // so only add the bottom one (so we dont block the space for the next pod)
                        add(homePositions.maxByOrNull { it.y }!!)
                    } else {
                        addAll((homePositions - podsInRoom.map { it.pos }.toSet()))
                    }
                }
            }
        }

    fun dijkstra(): Int {
        // should start with the possible moves for the first four pods
        // cost vs amphipod
        val frontier = mutableListOf<Pair<Int, Amphipod>>()

        // where we are vs where we came from
        val origin = mutableMapOf<Pos2D, Pos2D>()

        val costs = mutableMapOf<Amphipod, Int>()

        // add the pods that can currently move
        frontier.addAll(pods.filter { it.pos.y == 2 }.map { 0 to it })

        // We add all the pods of all the types to the frontier
        // When we pop one of them, the possibleLocations will be for that type
        // We always gets possible locations for the same type
        // But then, what does this mean for the costs we keep track of?
        // It might actually work rn, but we'll see

        while (frontier.isNotEmpty()) {
            frontier.sortByDescending { it.first }
            val cur = frontier.removeLast().second
            if (allAmphipodsAreHome()) {
                break
            }

            // For every pod in the frontier, check all the moves they can make
            // From all their moves, select the one with the lowest cost

            // Go through the possible locations of a pod that can currently move
            for (nxtPos in possibleLocations(cur)) {
                // calculate cost of position
                val cost = cur.pos.manhattan(nxtPos) * cur.energy()
                if (cur !in costs || cost < costs[cur]!!) {
                    frontier.add(cost to cur)
                    origin[cur.pos] = nxtPos
                    costs[cur] = cost
                }
            }
            // Move the pod, recalculate which pods can move
        }

        return costs.values.sum() // ??
//        return costs[goal]!!
    }

    // what to return?
    // list of pairs with <pod, new pos>?
    // list of positions?
    // list of positions and movement costs?
    fun movablePods(): List<Pos2D> =
        buildList {
            pods.forEach { pod ->
                pod.pos.neighbors().forEach { n ->
                    if (isOpenSpace(n)) {
                        add(n)
                    }
                }
            }
        }

    private fun isOccupied(spot: Pos2D): Amphipod? =
        pods.find { it.pos.x == spot.x && it.pos.y == spot.y }

    private fun isEntrance(spot: Pos2D): Boolean =
        entrances.contains(spot)

    private fun isOpenSpace(spot: Pos2D): Boolean {
        return when {
            spot.x < 0 || spot.x >= w || spot.y < 0 || spot.y >= h -> false
            isOccupied(spot) != null -> false
            isEntrance(spot) -> false
            else -> !walls[spot.y][spot.x]
        }
    }
}

fun main() {
    val files = setOf(
        Triple("test", 0, 0),
//        Triple("input", 0, 0)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val input = readInput("day$DAY/$file")

        val h = input.size
        val w = input[0].length

        val walls = Array(h) { Array(w) { false } }

        val roomA = Room(Pos2D(3, 2), 1, 2)
        val roomB = Room(Pos2D(5, 2), 1, 2)
        val roomC = Room(Pos2D(7, 2), 1, 2)
        val roomD = Room(Pos2D(9, 2), 1, 2)
        val rooms = listOf(roomA, roomB, roomC, roomD)
        val hallway = Room(Pos2D(1, 1), 11, 1)

        val amphipods = mutableListOf<Amphipod>()

        for (y in 0 until h) {
            for (x in 0 until w) {
                when (if (x >= input[y].length) ' ' else input[y][x]) {
                    '#', ' ' -> walls[y][x] = true
                    'A' -> amphipods.add(Amphipod(Type.A, Pos2D(x, y), roomA))
                    'B' -> amphipods.add(Amphipod(Type.B, Pos2D(x, y), roomB))
                    'C' -> amphipods.add(Amphipod(Type.C, Pos2D(x, y), roomC))
                    'D' -> amphipods.add(Amphipod(Type.D, Pos2D(x, y), roomD))
                }
            }
        }

        val burrow = Burrow(walls, rooms, hallway, w, h, amphipods)
        burrow.print()
        burrow.dijkstra()

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            0
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            0
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
