package com.cjj.re.segment

interface Segment {
    fun getSegment(): String

    companion object {
        fun getSegment(segments: List<Segment>): String {
            return segments.joinToString(" ") { it.getSegment() }
        }
    }
}