package day22

import measure
import readInput
import showtime

private const val DAY = 22

data class Region(
    val xr: IntRange,
    val yr: IntRange,
    val zr: IntRange,
    // keep count of on and off cubes in this region
    var amountOn: Long = 0L
) {

    override fun toString(): String = "Region($xr,$yr,$zr) [$amountOn on]"

    fun turnOn(turnOn: Boolean): Region {
        if (turnOn) {
            amountOn = ((xr.last - xr.first + 1) * (yr.last - yr.first + 1) * (zr.last - zr.first + 1)).toLong()
        }
        return this
    }

    fun clone(): Region = Region(xr, yr, zr, amountOn)

    fun contains(x: Int, y: Int, z: Int): Boolean =
        x in xr && y in yr && z in zr

    // the other region is a newer region that comes on top
    fun overlap(other: Region) {
        var overlap = 0L
        for (x in xr) {
            for (y in yr) {
                for (z in zr) {
                    if (other.contains(x, y, z)) {
                        overlap += 1L
                    }
                }
            }
        }
        println(" overlap between $this and $other: $overlap")
        if (overlap > 0L) {
            if (amountOn == 0L) {
                // nothing happens, keep amountOn of top layer (other)
            } else {
                // both when other.amountOn == 0 and when it isn't
                amountOn -= overlap
            }
        }
    }
}

class Reactor(val init: IntRange) {
    private val regions = mutableListOf<Region>()

    fun compute(): Long {
        regions.forEach { println(it) }

        // a) Compute overlap of last and each previous, sum the resulting amountOn
        println("a) last and each prev")
        val overlapPerRegion = mutableListOf<Region>()
        regions.forEach { overlapPerRegion.add(it.clone()) }
        val newest = overlapPerRegion.removeLast()
        overlapPerRegion.forEach { it.overlap(newest) }
        val a = overlapPerRegion.sumOf { it.amountOn }
        println("amountOn: $a")
        println()

        // b) Compute overlap of previous with those before it, sum
        println("b) prev and each before")
        val overlapPrevious = mutableListOf<Region>()
        regions.forEach { overlapPrevious.add(it.clone()) }
        overlapPrevious.removeLast()
        val beforeNewest = overlapPrevious.removeLast()
        overlapPrevious.forEach { it.overlap(beforeNewest) }
        overlapPrevious.add(beforeNewest)
        val b = overlapPrevious.sumOf { it.amountOn }
        println("amountOn: $b")
        println()

        println("e) prevprev and each before")
        val overlapPrevPrev = mutableListOf<Region>()
        regions.forEach { overlapPrevPrev.add(it.clone()) }
        overlapPrevPrev.removeLast()
        overlapPrevPrev.removeLast()
        val prevPrev = overlapPrevPrev.removeLast()
        overlapPrevPrev.forEach { it.overlap(prevPrev) }
        overlapPrevPrev.add(prevPrev)
        val e = overlapPrevPrev.sumOf { it.amountOn }
        println("amountOn: $e")
        println()

        // c) Compute consecutive overlap (current algo)
        println("c) consec overlap")
        val consecutiveOverlaps = mutableListOf<Region>()
        val allRegions = mutableListOf<Region>()
        regions.forEach { allRegions.add(it.clone()) }
        allRegions.forEach { r ->
            consecutiveOverlaps.forEach { it.overlap(r) }
            consecutiveOverlaps.add(r)
        }
        val c = consecutiveOverlaps.sumOf { it.amountOn }
        println("amountOn: $c")
        println()

        // d) c + b - a = actual amountOn
        val actual = c + b + (if (e > b) (e - b) * 10 else 0) - a
        println(actual)
        return actual
    }

    fun reboot(step: String) {
        val regex = Regex("""([a-z]+) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""")
        regex.matchEntire(step)
            ?.destructured
            ?.let { (on, x1, x2, y1, y2, z1, z2) ->
                var x1i = x1.toInt()
                var x2i = x2.toInt()
                var y1i = y1.toInt()
                var y2i = y2.toInt()
                var z1i = z1.toInt()
                var z2i = z2.toInt()
                if (init.first != 0 && init.last != 0) {
                    x1i = if (x1i < init.first) init.first else x1i
                    x2i = if (x2i > init.last) init.last else x2i
                    y1i = if (y1i < init.first) init.first else y1i
                    y2i = if (y2i > init.last) init.last else y2i
                    z1i = if (z1i < init.first) init.first else z1i
                    z2i = if (z2i > init.last) init.last else z2i
                }
                val xr = x1i..x2i
                val yr = y1i..y2i
                val zr = z1i..z2i
                val newRegion = Region(xr, yr, zr).turnOn(on == "on")
                regions.add(newRegion)
            }
    }
}

fun main() {
    val files = setOf(
        Triple("test", 39, 0),
//        Triple("test2", 590784, 0),
//        Triple("test2", 0, 2758514936282235),
//        Triple("input", 546724, 0)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")
        val input = readInput("day$DAY/$file")

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            val reactor = Reactor(-50..50)
            input.forEach { step -> reactor.reboot(step) }
            reactor.compute()
        }
        println(" expected: $ans1, actual: $part1")

//        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
//            val reactor = Reactor(0..0)
//            input.forEach { step -> reactor.reboot(step) }
//            reactor.cubesOn()
//        }
//        println(" expected: $ans2, actual: $part2\n")
    }
}
