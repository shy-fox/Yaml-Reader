package io.shiromi.samlkt.tools

class Range(private val start: Int, private val end: Int) {
    fun start(): Int {
        return start
    }

    fun end(): Int {
        return end
    }

    fun range(): Int {
        return end - start
    }

    override fun toString(): String {
        return String.format("Range: { start: %s, end: %s; range: %s }", start, end, range())
    }
}