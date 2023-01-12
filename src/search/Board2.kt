package search

/*
Bingo board of size x size, implemented as a custom collection, and iterable.
The overridden methods aren't even used, and I should probably have implemented
it as a SortedMap. Nonetheless, it was good practice.
 */
class Board(override val size: Int) : Iterable<Board.Cell>, Collection<Board.Cell> {

    class BoardIterator(
        private val board: Board
    ) : Iterator<Cell> {

        private var index = 0
        private var lastCell: Cell? = null

        override fun hasNext(): Boolean = index < board.size

        override fun next(): Cell {
            if (index >= board.size) throw IndexOutOfBoundsException()

            lastCell = board[index]
            index++
            return lastCell!!
        }
    }

    data class Cell(var nr: Int, var marked: Boolean) {
        override fun toString(): String = "Cell: $nr ($marked)"
    }

    private val cells = List(size * size) { Cell(0, false) }

    // Fill the board with specified values.
    // Needs to be an int collection that is as long as board size^2.
    fun populate(ints: Collection<Int>): Board {
        if (ints.size != size * size) {
            println("Uh oh, the array needs to contain as many elements as a board can fit.")
        }

        for ((index, nr) in ints.withIndex()) {
            cells[index].nr = nr
        }
        return this
    }

    // Marks off specified nr if it's on this board, returns whether it was marked.
    // NB: the board should contain unique numbers, so this function will return after the first nr has been found
    fun mark(nr: Int): Boolean {
        for (cell in cells) {
            if (cell.nr == nr) {
                cell.marked = true
                return true
            }
        }
        return false
    }

    private fun containsMarkedRow(): Boolean {
        for (row in cells.chunked(size)) {
            var wholeRowMarked = true
            if (row.count { it.marked } != size) {
                wholeRowMarked = false
            }
            if (wholeRowMarked) {
                return true
            }
        }
        return false
    }

    private fun containsMarkedCol(): Boolean {
        val matrix = cells.chunked(size)
        for (col in 0 until size) {
            var wholeColMatches = true
            for (row in 0 until size) {
                if (!matrix[row][col].marked) {
                    wholeColMatches = false
                    break
                }
            }
            if (wholeColMatches) {
                return true
            }
        }
        return false
    }

    // Returns whether this board won, "Bingo!"
    fun bingo(): Boolean = containsMarkedRow() || containsMarkedCol()

    fun score(): Int = cells.filter { !it.marked }.sumOf { it.nr }

    operator fun get(index: Int): Cell {
        return cells[index]
    }

    override fun iterator(): Iterator<Cell> {
        return BoardIterator(this)
    }

    override fun contains(element: Cell): Boolean {
        val iterator = iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.nr == element.nr && next.marked == element.marked) {
                return true
            }
        }
        return false
    }

    override fun containsAll(elements: Collection<Cell>): Boolean {
        for (el in elements) {
            if (!contains(el)) {
                return false
            }
        }
        return true
    }

    // The board always has size*size Cells, with (0, false) as defaults
    // This function doesn't really make sense for a Board
    override fun isEmpty(): Boolean = false
}
