package com.cjj.re.wrapper

import androidx.sqlite.db.SimpleSQLiteQuery
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
    fun orderBy(property: KProperty<*>, isAsc: Boolean) = apply {
        val columnBean = ColumnBean.byProperty(property)
        orderBy(columnBean.tableName, columnBean.columnName, isAsc)
    }

    /**
     * 排序
     */
    fun orderBy(tableName: String, column: String, isAsc: Boolean) = apply {
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
    override fun build(): String {

        val sb = StringBuilder()
        sb.append("SELECT ")

        if (queryColumns.isNotEmpty()) {
            val column = queryColumns.joinToString(",") {
                ColumnBean.byProperty(it).run {
                    "${tableName}.${columnName}"
                }
            }
            sb.append(column)
        } else {
            sb.append("$tableName.*")
        }

        sb.append(" FROM $tableName")
        if (joinList.isNotEmpty()) {
            sb.append(" ")
            sb.append(joinList.joinToString(" ") { it.build() })
            sb.append(" ")
        }
        sb.append(super.build())

        if (groupList.isNotEmpty()) {
            sb.append(" ")
            sb.append(GroupBySegment.getSegment(groupList))
            if (havingController.isNotEmpty()) {
                sb.append(" HAVING ")
                sb.append(havingController.getSegment())
            }
        }
        if (orderByList.isNotEmpty()) {
            sb.append(" ")
            sb.append(OrderBySegment.getSegment(orderByList))
        }
        if (limit != null) {
            sb.append(" LIMIT $limit")
            if (offset != null) {
                sb.append(" OFFSET $offset")
            }
        }

        return sb.toString()
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