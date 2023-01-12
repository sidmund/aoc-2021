package search

class LinkedList<T> : Iterable<T>, Collection<T>, MutableIterable<T>, MutableCollection<T> {

    // data class mainly should hold values, not behavior
    // very useful for destructuring: val (value, next) = someNode
    data class Node<T>(val value: T, var next: Node<T>? = null) {
        override fun toString(): String = "$value -> ${next?.toString()}"
    }

    // accessible outside, but setting it only inside this class
    override var size = 0
        private set

    private var head: Node<T>? = null
    private var tail: Node<T>? = null

    // Add value to front of list
    fun push(value: T): LinkedList<T> {
        /*
        val node = Node(value)
        node.next = head
        head = node
        // These 3 in a single line:
        */
        head = Node(value, head)
        if (tail == null) {
            tail = head
        }
        size++
        return this
    }

    // Add value to end of list
    fun append(value: T): LinkedList<T> {
        if (isEmpty()) {
            return push(value)
        }
        tail?.next = Node(value)
        tail = tail?.next
        size++
        return this
    }

    fun nodeAt(index: Int): Node<T>? {
        var curNode = head
        var i = 0
        while (curNode != null && i < index) {
            curNode = curNode.next
            i++
        }
        return curNode
    }

    fun insert(value: T, afterNode: Node<T>): LinkedList<T> {
        if (tail == afterNode) {
            return append(value)
        }
        afterNode.next = Node(value, afterNode.next)
        size++
        return this
    }

    // Removes the head, returns the value of said element
    fun pop(): T? {
        if (!isEmpty()) {
            size--
        }
        val result = head?.value
        head = head?.next
        if (isEmpty()) {
            tail = null
        }
        return result
    }

    fun removeLast(): T? {
        if (isEmpty()) {
            return null
        }
        size--
        val result = tail?.value
        if (isEmpty()) {
            tail = null; head = null
        } else {
            tail = nodeAt(size - 1)
            tail?.next = null
        }
        return result
    }

    fun removeAfter(node: Node<T>): T? {
        if (isEmpty()) {
            return null
        }
        val result = node.next?.value
        node.next = node.next?.next
        if (node.next == tail) {
            tail = node
        }
        size--
        return result
    }

    override fun isEmpty(): Boolean = size == 0

    override fun toString(): String = if (isEmpty()) "Empty list" else head.toString()

    override fun iterator(): MutableIterator<T> {
        return LinkedListIterator(this)
    }

    override fun contains(element: T): Boolean {
        for (node in this) {
            if (node == element) {
                return true
            }
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (e in elements) {
            if (!contains(e)) {
                return false
            }
        }
        return true
    }

    override fun add(element: T): Boolean {
        append(element)
        return true
    }

    override fun addAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            append(element)
        }
        return true
    }

    override fun clear() {
        head = null
        tail = null
        size = 0
    }

    override fun remove(element: T): Boolean {
        val iterator = iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item == element) {
                iterator.remove()
                return true
            }
        }
        return false
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        for (element in elements) {
            result = remove(element) || result
        }
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (!elements.contains(item)) {
                iterator.remove()
                result = true
            }
        }
        return result
    }
}
