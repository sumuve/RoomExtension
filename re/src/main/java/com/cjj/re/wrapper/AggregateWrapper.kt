package com.cjj.re.wrapper

import androidx.sqlite.db.SimpleSQLiteQuery
import com.cjj.re.condition.WhereConditionController
import com.cjj.re.keys.AggregateFunctions
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

    override fun build(): String {

        val sb = StringBuilder()
        if (function == AggregateFunctions.COUNT && column == null) {
            sb.append("SELECT ${function.value}(*)")
        } else {
            sb.append("SELECT ${function.value}($tableName.$columnName)")
        }

        sb.append(" FROM $tableName")
        if (joinList.isNotEmpty()) {
            sb.append(" ")
            sb.append(joinList.joinToString(" ") { it.build() })
            sb.append(" ")
        }
        sb.append(super.build())
        return sb.toString()
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