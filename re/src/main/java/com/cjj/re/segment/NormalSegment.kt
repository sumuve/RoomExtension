package com.cjj.re.segment

import com.cjj.re.bean.ColumnBean
import com.cjj.re.keys.SqlFunctions
import com.cjj.re.keys.SqlKeyword
import com.cjj.re.wrapper.Wrapper
import kotlin.reflect.KProperty

data class NormalSegment(
    override val condition: String,
    val column: String,
    val operator: String,
    val value: Any?,
    val function: SqlFunctions? = null,
    val tableName: String? = null,
) : ConditionSegment {

    override fun getSegment(isFormat: Boolean): String {
        val segment = buildString {
            if (function != null) {
                append(function.value)
                append("(")
            }
            if (tableName != null) {
                append(tableName)
                append(".")
                append(column)
            } else {
                append(column)
            }
            if (function != null) {
                append(")")
            }
            append(" ")
            append(operator)
            append(" ")
            formatValue(this, isFormat)
        }

        return segment

    }


    private fun formatValue(sb: StringBuilder, isFormat: Boolean): Unit = with(sb) {
        if (operator == SqlKeyword.BETWEEN || operator == SqlKeyword.NOT_BETWEEN) {
            if (value is Pair<*, *>) {
                if (isFormat) {
                    append(value.first)
                    append(" ")
                    append(SqlKeyword.AND)
                    append(" ")
                    append(value.second)
                } else {
                    append("? ")
                    append(SqlKeyword.AND)
                    append(" ?")
                }
                return@with
            }
        }
        when (value) {
            is String, is Number, is Char, is Boolean -> {
                append(if (isFormat) value.toString() else "?")
            }

            null -> {
                append("")
            }

            is Wrapper<*> -> {
                append("(")
                append(value.build(isFormat))
                append(")")
            }

            is Array<*> -> {
                append("(")
                append(formatArrayValue(value.asList(), isFormat))
                append(")")
            }

            is Collection<*> -> {
                append("(")
                append(formatArrayValue(value, isFormat))
                append(")")
            }

            is KProperty<*> -> {
                val column = ColumnBean.byProperty(value)
                append(column.tableName)
                append(".")
                append(column.columnName)
            }

            else -> {
                append(if (isFormat) value.toString() else "?")
            }
        }
    }

    private fun formatArrayValue(value: Iterable<Any?>, isFormat: Boolean): String {
        return value.joinToString(",") {
            when (it) {
                is String, is Number, is Char, is Boolean -> {
                    if (isFormat) value.toString() else "?"
                }

                is Iterable<*> -> {
                    formatArrayValue(it, isFormat)
                }

                is Array<*> -> {
                    formatArrayValue(it.asList(), isFormat)
                }

                is Wrapper<*> -> {
                    it.build(isFormat)
                }

                is ByteArray -> formatArrayValue(it.asList(), isFormat)
                is CharArray -> formatArrayValue(it.asList(), isFormat)
                is ShortArray -> formatArrayValue(it.asList(), isFormat)
                is IntArray -> formatArrayValue(it.asList(), isFormat)
                is LongArray -> formatArrayValue(it.asList(), isFormat)
                is FloatArray -> formatArrayValue(it.asList(), isFormat)
                is DoubleArray -> formatArrayValue(it.asList(), isFormat)
                is BooleanArray -> formatArrayValue(it.asList(), isFormat)

                else -> {
                    if (isFormat) it.toString() else "?"
                }
            }
        }
    }
}
