package day23

import println
import readInput

private const val DAY_ID = "23"

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.map {
            val (left, right) = it.split("-")
            left to right
        }

        val adj = mutableMapOf<String, MutableSet<String>>()
        for ((left, right) in pairs) {
            adj.getOrPut(left) { mutableSetOf() } += right
            adj.getOrPut(right) { mutableSetOf() } += left
        }

        val ans = mutableSetOf<List<String>>()
        for (u in adj.keys) {
            for (v in adj.keys) {
                if (u != v && v in adj[u]!!) {
                    val common = adj[u]!!.intersect(adj[v]!!)
                    for (w in common) {
                        val candidate = listOf(u, v, w).sorted()
                        if (candidate.any { it.startsWith("t") }) {
                            ans += candidate
                        }
                    }
                }
            }
        }
        return ans.size
    }

    fun part2(input: List<String>): String {
        val pairs = input.map {
            val (left, right) = it.split("-")
            left to right
        }

        val adj = mutableMapOf<String, MutableSet<String>>()
        for ((left, right) in pairs) {
            adj.getOrPut(left) { mutableSetOf() } += right
            adj.getOrPut(right) { mutableSetOf() } += left
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
    check(part2(readInput("day$DAY_ID/Day${DAY_ID}_test")).println() == "co,de,ka,ta")

    val input = readInput("day$DAY_ID/Day$DAY_ID")
    part1(input).println() // answer 1400
    part2(input).println() // answer am,bc,cz,dc,gy,hk,li,qf,th,tj,wf,xk,xo
}
