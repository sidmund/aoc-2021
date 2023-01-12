package search
//
//import kotlin.math.exp
//
///*
//make an openlist containing only the starting node
//   make an empty closed list
//   while (the destination node has not been reached):
//       consider the node with the lowest f score in the open list
//       if (this node is our destination node) :
//           we are finished
//       if not:
//           put the current node in the closed list and look at all of its neighbors
//           for (each neighbor of the current node):
//               if (neighbor has lower g value than current and is in the closed list) :
//                   replace the neighbor with the new, lower, g value
//                   current node is now the neighbor's parent
//               else if (current g value is lower and this neighbor is in the open list ) :
//                   replace the neighbor with the new, lower, g value
//                   change the neighbor's parent to our current node
//
//               else if this neighbor is not in both lists:
//                   add it to the open list and set its g
// */
//
//fun astar() {
//    val frontier = Queue<Pair<Int,Int>>(0)
//    frontier.add(0 to 0)
//    val explored = mutableSetOf<Pair<Int,Int>>()
//    explored.add(0 to 0)
//
//    while (!frontier.isEmpty()) {
//        val cur = frontier.poll()
//        for (next in neighbors(cur)) {
//            if (next !in explored) {
//                frontier.add(next)
//                explored.add(next)
//            }
//        }
//    }
//}