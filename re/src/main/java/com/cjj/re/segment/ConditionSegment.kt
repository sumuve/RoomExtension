package com.cjj.re.segment

interface ConditionSegment : Segment {
    val condition: String

    companion object {
        fun getSegment(normals: List<ConditionSegment>): String {
            val sb = StringBuilder()
            normals.forEachIndexed { index, it ->
                if (index != 0) {
                    sb.append("${it.condition} ${it.getSegment()}")
                } else {
                    sb.append(it.getSegment())
                }
                if (index != normals.size - 1) {
                    sb.append(" ")
                }
            }
            return sb.toString()
        }
    }
}