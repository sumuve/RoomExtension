package com.cjj.re.wrapper

import com.cjj.re.condition.WhereConditionController
import com.cjj.re.keys.AggregateFunctions
import com.cjj.re.keys.SqlKeyword
import com.cjj.re.util.TableUtils
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class AggregateWrapper(private val function: AggregateFunctions, val column: KProperty<*>?, kClass: KClass<*>) :
    Wrapper<AggregateWrapper>(kClass) {

    override val alias: Boolean get() = true
    protected val joinList = arrayListOf<JoinWrapper>()
    private val columnName = column?.let { TableUtils.getColumnName(it) }
    fun join(
        joinTableClass: KClass<*>,
        joinTableColumn: KProperty<*>,
        sourceColumn: KProperty<*>
    ) = apply {
        join(joinTableClass, on = {
            eq(joinTableColumn, sourceColumn)
        })
    }


    fun join(joinTableClass: KClass<*>, on: (WhereConditionController.() -> Unit)) = apply {
        val wrapper = JoinWrapper(joinTableClass)
        joinList.add(wrapper)
        wrapper.condition(on)
    }

    override fun build(isFormat: Boolean): String {

        val sql = buildString {
            if (function == AggregateFunctions.COUNT && column == null) {
                append(SqlKeyword.SELECT)
                append(" ")
                append(function.value)
                append("(*)")
            } else {
                append(SqlKeyword.SELECT)
                append(" ")
                append(function.value)
                append("(")
                append(tableName)
                append(".")
                append(columnName)
                append(")")
            }
            append(" ")
            append(SqlKeyword.FROM)
            append(" ")
            append(tableName)
            if (joinList.isNotEmpty()) {
                append(" ")
                append(joinList.joinToString(" ") { it.build(isFormat) })
                append(" ")
            }
            append(super.build(isFormat))
        }

        return sql
    }


    override fun getSqlBindArgs(): List<Any> {
        val args = arrayListOf<Any>()

        joinList.forEach {
            args.addAll(it.values)
        }
        args.addAll(values)
        return args
    }
}