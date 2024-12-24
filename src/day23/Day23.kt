package day23

import println
import readInput

private const val DAY_ID = "23"

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.map {
            val (u, v) = it.split("-")
            u to v
        }

        val adj = mutableMapOf<String, MutableSet<String>>()
        for ((u, v) in pairs) {
            adj.getOrPut(u) { mutableSetOf() } += v
            adj.getOrPut(v) { mutableSetOf() } += u
        }

        val ans = mutableSetOf<List<String>>()
        for (u in adj.keys) {
            for (v in adj[u]!!) {
                for (w in adj[v]!!) {
                    if (w != u && w in adj[u]!!) {
                        val triplet = listOf(u, v, w)
                        if (triplet.any { it.startsWith("t") }) {
                            ans += triplet.sorted()
                        }
                    }
                }
            }
        }
        return ans.size
    }

    fun part2(input: List<String>): String {
        val pairs = input.map {
            val (u, v) = it.split("-")
            u to v
        }

        val adj = mutableMapOf<String, MutableSet<String>>()
        for ((u, v) in pairs) {
            adj.getOrPut(u) { mutableSetOf() } += v
            adj.getOrPut(v) { mutableSetOf() } += u
        }

        // Search for the maximum clique in a graph.
        // Clique is a complete subgraph of a graph, i.e. a subset of vertices such that every two distinct vertices are adjacent.
        val cliques = mutableSetOf<List<String>>()
        fun findCliques(node: String, connected: Set<String>) {
            val clique = connected.sorted()
            if (clique in cliques) {
                return
            }

            cliques += clique
            for (neighbor in adj[node]!!) {
                // `neighbor` must be connected to all nodes in `connected`, i.e.
                // `connected` is a subset of `adj[neighbor]`
                if (neighbor !in connected && connected.all { it in adj[neighbor]!! }) {
                    findCliques(neighbor, connected + neighbor)
                }
            }
        }

        for (u in adj.keys) {
            // find all cliques that contain node u
            findCliques(u, setOf(u))
        }

        return cliques.maxBy { it.size }.joinToString(",")
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_test")) == 7)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test")) == "co,de,ka,ta")

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 1400
    part2(input).println() // answer am,bc,cz,dc,gy,hk,li,qf,th,tj,wf,xk,xo
}
