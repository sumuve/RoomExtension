package com.cjj.re.segment

interface ConditionSegment : Segment {
    val condition: String

    companion object {
        fun getSegment(normals: List<ConditionSegment>, isFormat: Boolean): String {
            val sb = StringBuilder()
            normals.forEachIndexed { index, it ->
                if (index != 0) {
                    sb.append(it.condition)
                    sb.append(" ")
                    sb.append(it.getSegment(isFormat))
                } else {
                    sb.append(it.getSegment(isFormat))
                }
                if (index != normals.size - 1) {
                    sb.append(" ")
                }
            }
            return sb.toString()
        }
    }
}