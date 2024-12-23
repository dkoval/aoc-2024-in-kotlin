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

        // Bron-Kerbosch algorithm to find the maximum clique in a graph
        // (GitHub Copilot generated this implementation)
        fun findMaxClique(adj: Map<String, Set<String>>): Set<String> {
            var maxClique = emptySet<String>()
            fun bronKerbosch(R: Set<String>, _P: Set<String>, _X: Set<String>) {
                val P = _P.toMutableSet()
                val X = _X.toMutableSet()

                if (P.isEmpty() && X.isEmpty()) {
                    if (R.size > maxClique.size) {
                        maxClique = R
                    }
                    return
                }

                val pivot = (P + X).first()
                for (v in P - adj[pivot]!!) {
                    bronKerbosch(R + v, P.intersect(adj[v]!!), X.intersect(adj[v]!!))
                    P -= v
                    X += v
                }
            }

            bronKerbosch(emptySet(), adj.keys, emptySet())
            return maxClique
        }

        return findMaxClique(adj).sorted().joinToString(",")
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day$DAY_ID/Day${DAY_ID}_test")) == 7)
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test")) == "co,de,ka,ta")

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 1400
    part2(input).println() // answer am,bc,cz,dc,gy,hk,li,qf,th,tj,wf,xk,xo
}
