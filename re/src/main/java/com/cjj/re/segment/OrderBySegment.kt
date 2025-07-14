package com.cjj.re.segment

import com.cjj.re.keys.SqlKeyword

data class OrderBySegment(val tableName: String, val column: String, val type: String) : Segment {
    override fun getSegment(isFormat: Boolean): String {
        return "$tableName.$column $type"
    }

    companion object {
        fun getSegment(orders: Collection<OrderBySegment>, isFormat: Boolean): String {
            return "${SqlKeyword.ORDER_BY} ${orders.joinToString(", ") { it.getSegment(isFormat) }}"
        }
    }
}
