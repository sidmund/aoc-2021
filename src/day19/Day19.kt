package day19

import measure
import readInput
import showtime
import kotlin.math.abs

private const val DAY = 19

data class Vec3(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun manhattan(other: Vec3): Int = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
}

data class Beacon(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun rotateX(n: Int): Beacon {
        var newB = this
        repeat(n % 4) {
            newB = Beacon(newB.x, newB.z, -newB.y)
        }
        return newB
    }

    fun rotateY(n: Int): Beacon {
        var newB = this
        repeat(n % 4) {
            newB = Beacon(newB.z, newB.y, -newB.x)
        }
        return newB
    }

    fun rotateZ(n: Int): Beacon {
        var newB = this
        repeat(n % 4) {
            newB = Beacon(newB.y, -newB.x, newB.z)
        }
        return newB
    }

    fun flipX(doFlip: Boolean): Beacon = if (doFlip) Beacon(-x, y, z) else this
    fun flipY(doFlip: Boolean): Beacon = if (doFlip) Beacon(x, -y, z) else this
    fun flipZ(doFlip: Boolean): Beacon = if (doFlip) Beacon(x, y, -z) else this
}

data class Scanner(
    val id: Int,
    val beacons: MutableSet<Beacon> = mutableSetOf()
) {

    fun parse(line: String) {
        val regex = Regex("""(.+),(.+),(.+)""")
        regex.matchEntire(line)
            ?.destructured
            ?.let { (x, y, z) ->
                beacons.add(Beacon(x.toInt(), y.toInt(), z.toInt()))
            }
    }

    // Compute all possible orientations
    private fun orientations(): MutableList<List<Beacon>> {
        val orientations = mutableListOf<List<Beacon>>()
        for (rotX in 0..3) {
            for (rotY in 0..3) {
                for (rotZ in 0..3) {
                    for (flipX in setOf(false, true)) {
                        for (flipY in setOf(false, true)) {
                            for (flipZ in setOf(false, true)) {
                                orientations.add(beacons.map {
                                    it
                                        .rotateX(rotX)
                                        .rotateY(rotY)
                                        .rotateZ(rotZ)
                                        .flipX(flipX)
                                        .flipY(flipY)
                                        .flipZ(flipZ)
                                })
                            }
                        }
                    }
                }
            }
        }
        return orientations
    }

    /*
    Overlap two scanners, return a map containing unique beacons with their detection count,
    null if there were less than 12 beacons in common for any shift
    Returns the shifted position of scanner and a map of unique beacons
     */
    private fun overlapPair(scanner: Scanner): Pair<Vec3, Map<Beacon, Int>>? {
        for (orientation in scanner.orientations()) {
            beacons.forEach { my ->
                orientation.forEach { their ->
                    val dx = my.x - their.x
                    val dy = my.y - their.y
                    val dz = my.z - their.z

                    val overlapping = mutableMapOf<Beacon, Int>()
                    overlapping.putAll(beacons.associateWith { 1 })
                    orientation.forEach { their2 ->
                        val newB = Beacon(their2.x + dx, their2.y + dy, their2.z + dz)
                        if (overlapping[newB] == null) {
                            overlapping[newB] = 1
                        } else {
                            overlapping[newB] = overlapping[newB]!! + 1
                        }
                    }
                    if (overlapping.count { it.value > 1 } >= 12) {
                        return Vec3(dx, dy, dz) to overlapping
                    }
                }
            }
        }
        return null
    }

    // return the set of beacons (>=12) that are common to this and other, null if none (<12)
    // from the perspective of this scanner
    fun common(other: Scanner): Pair<Vec3, Set<Beacon>>? {
        val result = overlapPair(other)
        return if (result == null) null else result.first to result.second.keys
    }
}

fun main() {
    val files = setOf(
        Triple("test", 79, 3621),
        Triple("input", 320, 9655) // takes ~5m
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val input = readInput("day$DAY/$file").filter { it != "" }
        val scanners = mutableListOf<Scanner>()
        var cur: Scanner? = null
        for (s in input) {
            if (s.startsWith("---")) {
                if (cur != null) {
                    scanners.add(cur)
                }
                cur = Scanner(s.split(" ")[2].toInt())
            } else {
                cur?.parse(s)
            }
        }
        if (cur != null) {
            scanners.add(cur)
        }

        val coords = mutableListOf<Vec3>()

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            // Construct a scanner based on total overlap, which should have a set of all unique beacons
            var fullScanner = scanners[0]
            coords.add(Vec3(0, 0, 0))

            var unchecked = (1 until scanners.size).toList()
            while (unchecked.isNotEmpty()) {
                val curScanner = scanners[unchecked[0]]
                val common = fullScanner.common(curScanner)

                if (common != null) {// >= 12 in common
                    fullScanner = Scanner(-1, common.second.toMutableSet())
                    coords.add(Vec3(common.first.x, common.first.y, common.first.z))
                    unchecked = unchecked.drop(1)
                } else { // < 12 in common, put current index at end, so we can check the next one
                    unchecked = unchecked.drop(1) + unchecked.take(1)
                }
            }

            fullScanner.beacons.size
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            val dists = mutableListOf<Int>()

            for (i in coords.indices) {
                for (j in coords.indices) {
                    dists.add(coords[i].manhattan(coords[j]))
                }
            }

            dists.maxOf { it }
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
