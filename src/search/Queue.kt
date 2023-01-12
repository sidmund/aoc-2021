package search

import java.util.LinkedList
import java.util.Queue

class Queue<T>(override val size: Int) : Queue<T> {
    private val queue = LinkedList<T>()

    override fun add(element: T): Boolean {
        return queue.add(element)
    }

    override fun contains(element: T): Boolean {
        return queue.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return queue.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return queue.addAll(elements)
    }

    override fun clear() {
        queue.clear()
    }

    override fun iterator(): MutableIterator<T> {
        return queue.iterator()
    }

    override fun remove(): T {
        return queue.removeFirst()
    }

    override fun remove(element: T): Boolean {
        return queue.remove(element)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return queue.removeAll(elements.toSet())
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return queue.retainAll(elements.toSet())
    }

    override fun offer(e: T): Boolean {
        return queue.add(e)
    }

    override fun poll(): T {
        return queue.removeFirst()
    }

    override fun element(): T {
        return peek()
    }

    override fun peek(): T {
        return queue.peekFirst()
    }
}