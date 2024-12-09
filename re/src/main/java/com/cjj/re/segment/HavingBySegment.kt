package com.cjj.re.segment

import com.cjj.re.keys.SqlKeyword

data class HavingBySegment(val tableName: String, val column: String) : Segment {
    override fun getSegment(): String {
        return "$tableName.$column"
    }

    companion object {
        fun getSegment(orders: List<HavingBySegment>): String {
            return "${SqlKeyword.GROUP_BY} ${orders.joinToString(", ") { it.getSegment() }}"
        }
    }
}
