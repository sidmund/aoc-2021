package day21

import measure
import readInput
import showtime
import kotlin.math.*

private const val DAY = 21

data class Player(
    var pos: Int,
    var score: Int = 0
) {
    companion object {
        private val regex = Regex("""Player (\d) starting position: (\d)""")

        fun parse(line: String): Player? =
            regex.matchEntire(line)
                ?.destructured
                ?.let { (_, x) ->
                    Player(
                        x.toInt()
                    )
                }
    }

    fun update(sum: Int) {
        pos = ((pos - 1 + sum) % 10) + 1
        score += pos
    }

    fun clone(): Player = Player(pos, score)

    fun cloneFrom(sum: Int): Player {
        val npos = ((pos - 1 + sum) % 10) + 1
        return Player(npos, score + npos)
    }
}

data class Game(val p1: Player, val p2: Player, var turn: Int = -1) {

    fun winner(): Int? =
        when {
            p1.score >= 21 -> 1
            p2.score >= 21 -> 2
            else -> null
        }

    fun branches(): List<Game> {
        if (winner() != null) {
            return emptyList()
        }

        turn++

        val branches = mutableListOf<Game>()
        for (d1 in 1..3) {
            for (d2 in 1..3) {
                for (d3 in 1..3) {
                    val sum = d1 + d2 + d3
                    if (turn % 2 == 0) {
                        branches.add(Game(p1.cloneFrom(sum), p2.clone(), 0))
                    } else {
                        branches.add(Game(p1.clone(), p2.cloneFrom(sum), -1))
                    }
                }
            }
        }
        return branches
    }
}

fun main() {
    val files = setOf(
        Triple("test", 739785, 444356092776315),
        Triple("input", 506466, 632979211251440)
    )

    files.forEach { (file, ans1, ans2) ->
        println("=== $file ===")

        val part1 = measure({ print("[ ${showtime(it)} ]") }) {
            val players = readInput("day$DAY/$file").map(Player::parse)

            var die = 0
            var dieRolls = 0
            var turn = 0

            while (players[0]!!.score < 1000 && players[1]!!.score < 1000) {
                val cur = players[turn % 2]!!

                val sum = (die + 1) + (die + 2) + (die + 3)
                die = (die + 3) % 100
                dieRolls += 3

                cur.update(sum)

                turn++
            }

            min(players[0]!!.score, players[1]!!.score).toLong() * dieRolls.toLong()
        }
        println(" expected: $ans1, actual: $part1")

        val part2 = measure({ print("[ ${showtime(it)} ]") }) {
            val players = readInput("day$DAY/$file").map(Player::parse)

            var universes = mutableMapOf(Game(players[0]!!, players[1]!!) to 1L)

            // We stop when all games have a winner
            while (universes.any { it.key.winner() == null }) {

                val branches = mutableMapOf<Game, Long>()

                // simulate each non-winning universe and branch off further
                universes.forEach { (game, occurence) ->
                    if (game.winner() == null) {
                        game.branches().forEach { branch ->
                            branches.merge(branch, occurence, Long::plus)
                        }
                    } else {
                        branches.merge(game, occurence, Long::plus)
                    }
                }

                universes = branches
            }

            val playerWins = universes.map { (game, occurence) -> game.winner()!! to occurence }
            val p1Wins = playerWins.filter { it.first == 1 }.fold(0L) { wins, (_, count) -> wins + count }
            val p2Wins = playerWins.filter { it.first == 2 }.fold(0L) { wins, (_, count) -> wins + count }
            max(p1Wins, p2Wins)
        }
        println(" expected: $ans2, actual: $part2\n")
    }
}
