package com.cjj.re.segment

interface Segment {
    fun getSegment(isFormat: Boolean): String

    companion object {
        fun getSegment(segments: List<Segment>, isFormat: Boolean): String {
            return segments.joinToString(" ") { it.getSegment(isFormat) }
        }
    }
}