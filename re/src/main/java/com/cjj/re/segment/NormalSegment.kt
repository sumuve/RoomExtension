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

    override fun getSegment(): String {
        val sb = StringBuilder()
        if (function != null) {
            sb.append(function.value)
            sb.append("(")
        }
        if (tableName != null) {
            sb.append(tableName)
            sb.append(".")
            sb.append(column)
        } else {
            sb.append(column)
        }
        if (function != null) {
            sb.append(")")
        }
        sb.append(" ")
        sb.append(operator)
        sb.append(" ")
        sb.append(formatValue())
        return sb.toString()

    }


    private fun formatValue(): String {
        if (operator == SqlKeyword.BETWEEN || operator == SqlKeyword.NOT_BETWEEN) {
            return " ? and ? "
        }
        if ((operator == SqlKeyword.IN || operator == SqlKeyword.NOT_IN) && value is Array<*>) {
            return "(${formatArrayValue(value.asList())})"
        }
        return when (value) {
            null -> {
                ""
            }

            is Wrapper<*> -> {
                "(${value.build()})"
            }

            is Array<*> -> {
                return "(${formatArrayValue(value.asList())})"
            }

            is Collection<*> -> {
                return "(${formatArrayValue(value)})"
            }

            is KProperty<*> -> {
                val column = ColumnBean.byProperty(value)
                return "${column.tableName}.${column.columnName}"
            }

            else -> {
                return " ? "
            }
        }
    }

    private fun formatArrayValue(value: Iterable<Any?>): String {
        return value.joinToString(",") {
            when (it) {
                is Iterable<*> -> {
                    formatArrayValue(it)
                }

                is Array<*> -> {
                    formatArrayValue(it.asList())
                }

                is Wrapper<*> -> {
                    it.build()
                }

                else -> {
                    " ? "
                }
            }
        }
    }
}
