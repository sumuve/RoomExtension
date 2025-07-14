package com.cjj.re.wrapper

import com.cjj.re.bean.ColumnBean
import com.cjj.re.condition.HavingConditionController
import com.cjj.re.condition.WhereConditionController
import com.cjj.re.keys.SqlKeyword
import com.cjj.re.segment.GroupBySegment
import com.cjj.re.segment.OrderBySegment
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class QueryWrapper(kClass: KClass<*>) :
    Wrapper<QueryWrapper>(kClass) {

    override val alias: Boolean get() = true
    protected val joinList = arrayListOf<JoinWrapper>()


    protected val groupList = linkedSetOf<GroupBySegment>()
    protected val orderByList = linkedSetOf<OrderBySegment>()
    protected val havingController: HavingConditionController by lazy {
        HavingConditionController(
            arrayListOf(),
            kClass
        )
    }

    private val queryColumns = arrayListOf<KProperty<*>>()


    private var limit: Int? = null
    private var offset: Int? = null
    private val distinct = false

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


    fun queryColumn(property: KProperty<*>) = apply {
        queryColumns.add(property)
    }

    /**
     * 升序排序
     */
    fun orderByAsc(property: KProperty<*>) = apply {
        val columnBean = ColumnBean.byProperty(property)
        orderBy(columnBean.tableName, columnBean.columnName, true)
    }

    /**
     * 倒序排序
     */
    fun orderByDesc(property: KProperty<*>) = apply {
        val columnBean = ColumnBean.byProperty(property)
        orderBy(columnBean.tableName, columnBean.columnName, false)
    }


    /**
     * 分组查询
     *
     * @param property 列
     * @param having 分组查询过滤条件
     *
     */
    fun groupBy(vararg property: KProperty<*>, having: (HavingConditionController.() -> Unit)? = null) = apply {
        val columns =
            property.map { ColumnBean.byProperty(it).let { column -> column.tableName to column.columnName } }
                .toTypedArray()
        groupBy(*columns, having = having)
    }

    /**
     * 分组查询
     *
     * @param columnInfos 列信息
     * @param having 分组查询过滤条件
     *
     */
    fun groupBy(vararg columnInfos: Pair<String, String>, having: (HavingConditionController.() -> Unit)? = null) =
        apply {
            columnInfos.forEach {
                groupList.add(
                    GroupBySegment(it.first, it.second)
                )
            }
            having?.invoke(this.havingController)
        }


    /**
     * 排序
     */
    fun orderBy(property: KProperty<*>, isAsc: Boolean = true) = apply {
        val columnBean = ColumnBean.byProperty(property)
        orderBy(columnBean.tableName, columnBean.columnName, isAsc)
    }

    /**
     * 排序
     */
    fun orderBy(tableName: String, column: String, isAsc: Boolean = true) = apply {
        orderByList.add(
            OrderBySegment(
                tableName, column, if (isAsc) SqlKeyword.ASC else SqlKeyword.DESC
            )
        )
    }

    /**
     * 查询范围
     */
    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    /**
     * 偏移量
     */
    fun offset(offset: Int) = apply {
        this.offset = offset
    }

    /**
     * Sql组装方法
     *
     * @return sql
     */
    override fun build(isFormat: Boolean): String {
        val sql = buildString {
            append(SqlKeyword.SELECT)
            append(" ")

            if (queryColumns.isNotEmpty()) {
                val column = queryColumns.joinToString(",") {
                    ColumnBean.byProperty(it).run {
                        "${tableName}.${columnName}"
                    }
                }
                append(column)
            } else {
                append(tableName)
                append(".*")
            }
            append(" ")
            append(SqlKeyword.FROM)
            append(" ")
            append(tableName)
            if (joinList.isNotEmpty()) {
                append(" ")
                append(joinList.joinToString(" ") { it.build(isFormat) })
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
            if (orderByList.isNotEmpty()) {
                append(" ")
                append(OrderBySegment.getSegment(orderByList, isFormat))
            }
            if (limit != null) {
                append(" ")
                append(SqlKeyword.LIMIT)
                append(" ")
                append(limit)
                if (offset != null) {
                    append(" ")
                    append(SqlKeyword.OFFSET)
                    append(" ")
                    append(offset)
                }
            }
        }
        return sql
    }

    /**
     * @return 参数列表
     */
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