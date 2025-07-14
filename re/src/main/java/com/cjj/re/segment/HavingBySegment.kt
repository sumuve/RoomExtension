package com.cjj.re.segment

import com.cjj.re.keys.SqlKeyword

data class HavingBySegment(val tableName: String, val column: String) : Segment {
    override fun getSegment(isFormat: Boolean): String {
        return "$tableName.$column"
    }

    companion object {
        fun getSegment(orders: List<HavingBySegment>, isFormat: Boolean): String {
            return "${SqlKeyword.GROUP_BY} ${orders.joinToString(", ") { it.getSegment(isFormat) }}"
        }
    }
}
