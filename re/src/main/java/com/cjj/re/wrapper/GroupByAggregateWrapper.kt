package com.cjj.re.wrapper

import com.cjj.re.bean.ColumnBean
import com.cjj.re.condition.HavingConditionController
import com.cjj.re.condition.WhereConditionController
import com.cjj.re.keys.AggregateFunctions
import com.cjj.re.keys.SqlKeyword
import com.cjj.re.segment.GroupBySegment
import com.cjj.re.segment.OrderBySegment
import com.cjj.re.util.TableUtils
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class GroupByAggregateWrapper(private val funtions: AggregateFunctions, val column: KProperty<*>, kClass: KClass<*>) :
    Wrapper<GroupByAggregateWrapper>(kClass) {

    override val alias: Boolean get() = true
    protected val joinList = arrayListOf<JoinWrapper>()
    private val columnName = TableUtils.getColumnName(column)


    protected val groupList = linkedSetOf<GroupBySegment>()
    protected val orderByList = linkedSetOf<OrderBySegment>()
    protected val havingController: HavingConditionController by lazy {
        HavingConditionController(
            arrayListOf(),
            kClass
        )
    }

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


    fun groupBy(vararg property: KProperty<*>, having: (HavingConditionController.() -> Unit)? = null) = apply {
        val columns =
            property.map {
                ColumnBean.byProperty(it)
                    .let { column -> column.tableName to column.columnName }
            }.toTypedArray()
        groupBy(*columns, having = having)
    }

    fun groupBy(vararg columnInfos: Pair<String, String>, having: (HavingConditionController.() -> Unit)? = null) =
        apply {
            columnInfos.forEach {
                groupList.add(
                    GroupBySegment(it.first, it.second)
                )
            }
            having?.invoke(this.havingController)
        }

    override fun build(isFormat: Boolean): String {

        val sql = buildString {
//        if (funtions == AggregateFunctions.COUNT) {
//            sb.append("SELECT *,${funtions.value}(*) as __group")
//        } else {
            append(SqlKeyword.SELECT)
            append(" *, ")
            append(funtions.value)
            append("(")
            append(tableName)
            append(".")
            append(columnName)
            append(") as __group ")
            append(SqlKeyword.FROM)
            append(" ")
            append(tableName)
            if (joinList.isNotEmpty()) {
                append(" ")
                append(joinList.joinToString(" ") { it.build(isFormat) })
                append(" ")
            }
            append(super.build(isFormat))
            if (groupList.isNotEmpty()) {
                append(" ")
                append(GroupBySegment.getSegment(groupList, isFormat))
                if (havingController.isNotEmpty()) {
                    append(" ")
                    append(SqlKeyword.HAVING)
                    append(" ")
                    append(havingController.getSegment(isFormat))
                }
            }
        }
        return sql
    }


    override fun getSqlBindArgs(): List<Any> {
        val args = arrayListOf<Any>()

        joinList.forEach {
            args.addAll(it.values)
        }
        args.addAll(values)
        if (havingController.isNotEmpty()) {
            args.addAll(havingController.values)
        }
        return args
    }
}