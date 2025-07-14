package com.cjj.re.condition

import com.cjj.re.bean.ColumnBean
import com.cjj.re.keys.SqlFunctions
import com.cjj.re.keys.SqlKeyword
import com.cjj.re.segment.ConditionSegment
import com.cjj.re.segment.NestedSegment
import com.cjj.re.segment.Segment
import com.cjj.re.util.TableUtils
import com.cjj.re.wrapper.QueryWrapper
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class ConditionController<out T : ConditionController<T, F>, F : SqlFunctions> internal constructor(
    val values: ArrayList<Any>,
    val tableClass: KClass<*>,
    connection: String = ""
) : Segment {
    protected val nestedSegment: NestedSegment = NestedSegment(connection)

    val tableName: String = TableUtils.getTableName(tableClass)

    @Suppress("UNCHECKED_CAST", "LeakingThis")
    private val thisT = this as T

    /**
     * 等于 = 值
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun eq(property: KProperty<*>, value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        eq(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    /**
     * 等于 = 值
     * @param tableName 表名
     * @param column 列名
     * @param value 值
     * @return this
     */
    open fun eq(column: String, value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.EQ, value, function, tableName)
        return thisT
    }

    /**
     * 包含 LIKE %值%
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun like(property: KProperty<*>, value: String, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        like(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun like(column: String, value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.LIKE, "%$value%", function, tableName)
        return thisT
    }

    /**
     * LIKE '值%'
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun likeRight(property: KProperty<*>, value: String, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        likeRight(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun likeRight(column: String, value: String, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.LIKE, "$value%", function, tableName)
        return thisT
    }

    /**
     * LIKE '%值'
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun likeLeft(property: KProperty<*>, value: String, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        likeLeft(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun likeLeft(column: String, value: String, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.LIKE, "%$value", function, tableName)

        return thisT
    }

    /**
     * NOT LIKE '%值%'
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun notLike(property: KProperty<*>, value: String, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        notLike(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun notLike(column: String, value: String, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.NOT_LIKE, "%$value%", function, tableName)
        return thisT
    }

    /**
     * NOT LIKE '值%'
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun notLikeRight(property: KProperty<*>, value: String, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        notLikeRight(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun notLikeRight(column: String, value: String, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.NOT_LIKE, "$value%", function, tableName)
        return thisT
    }

    /**
     * NOT LIKE '%值'
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun notLikeLeft(property: KProperty<*>, value: String, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        notLikeLeft(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun notLikeLeft(column: String, value: String, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.NOT_LIKE, "%$value", function, tableName)
        return thisT
    }

    /**
     * 不等于 <> 值
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun ne(property: KProperty<*>, value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        ne(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun ne(column: String, value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.NE, value, function, tableName)
        return thisT
    }

    /**
     * 大于 > 值
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun gt(property: KProperty<*>, value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        gt(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun gt(column: String, value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.GT, value, function, tableName)
        return thisT
    }

    /**
     * 大于等于 >= 值
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun ge(property: KProperty<*>, value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        ge(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun ge(column: String, value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.GE, value, function, tableName)
        return thisT
    }

    /**
     * 小于 < 值
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun lt(property: KProperty<*>, value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        lt(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun lt(column: String, value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.LT, value, function, tableName)
        return thisT
    }

    /**
     * 小于等于  <= 值
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun le(property: KProperty<*>, value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        le(columnBean.columnName, value, function, columnBean.tableName)
        return thisT
    }

    open fun le(column: String, value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.LE, value, function, tableName)
        return thisT
    }

    /**
     * 字段 IS NULL
     * <p>例: isNull("name")</p>
     * @param property 字段
     * @return this
     */
    open fun isNull(property: KProperty<*>): T {
        val columnBean = ColumnBean.byProperty(property)
        isNull(columnBean.columnName, columnBean.tableName)
        return thisT
    }

    open fun isNull(column: String, tableName: String? = null): T {
        addSegment(column, SqlKeyword.IS_NULL, null, function = null, tableName)
        return thisT
    }

    /**
     * 字段 IS NOT NULL
     * @param property 字段
     * @return this
     */
    open fun isNotNull(property: KProperty<*>): T {
        val columnBean = ColumnBean.byProperty(property)
        isNotNull(columnBean.columnName, columnBean.tableName)
        return thisT
    }

    open fun isNotNull(column: String, tableName: String? = null): T {
        addSegment(column, SqlKeyword.IS_NOT_NULL, null, function = null, tableName)
        return thisT
    }

    /**
     * 字段 IN (value[0], value[1], ...)
     * <p>例: in(UserDao::id, 1, 2, 3, 4, 5)</p>
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun `in`(property: KProperty<*>, vararg value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        `in`(columnBean.columnName, value, function = function, tableName = columnBean.tableName)
        return thisT
    }

    open fun `in`(column: String, vararg value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.IN, value, function, tableName)
        return thisT
    }

    /**
     * 字段 NOT IN (value[0], value[1], ...)
     * <p>例: notIn(UserDao::id, 1, 2, 3, 4, 5)</p>
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun notIn(property: KProperty<*>, vararg value: Any, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        notIn(columnBean.columnName, value, function = function, tableName = columnBean.tableName)
        return thisT
    }

    open fun notIn(column: String, vararg value: Any, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.NOT_IN, value, function, tableName)
        return thisT
    }

    /**
     * glob 值
     * @param property 字段
     * @param value 值
     * @return this
     */
    open fun glob(property: KProperty<*>, value: String, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        glob(columnBean.columnName, value, function = function, columnBean.tableName)
        return thisT
    }

    open fun glob(column: String, value: String, function: F? = null, tableName: String? = null): T {
        addSegment(column, SqlKeyword.GLOB, value, function, tableName)
        return thisT
    }

    /**
     * BETWEEN start AND end
     * @param property 字段
     * @param start 起始值
     * @param end 结束值
     * @return this
     */
    open fun between(property: KProperty<*>, start: Number, end: Number, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        between(columnBean.columnName, start, end, function, columnBean.tableName)
        return thisT
    }

    open fun <V> between(
        property: KProperty<*>,
        range: OpenEndRange<V>,
        function: F? = null
    ): T where V : Number, V : Comparable<V> {
        val columnBean = ColumnBean.byProperty(property)
        between(columnBean.columnName, range.start, range.endExclusive, function, columnBean.tableName)
        return thisT
    }

    open fun between(
        column: String,
        start: Number,
        end: Number,
        function: F? = null,
        tableName: String? = null
    ): T {
        addSegment(column, SqlKeyword.BETWEEN, start to end, function, tableName)
        return thisT
    }

    /**
     * NOT BETWEEN start AND end
     * @param property 字段
     * @param start 起始值
     * @param end 结束值
     * @return this
     */
    open fun notBetween(property: KProperty<*>, start: Number, end: Number, function: F? = null): T {
        val columnBean = ColumnBean.byProperty(property)
        notBetween(columnBean.columnName, start, end, function, columnBean.tableName)
        return thisT
    }

    open fun <V> notBetween(
        property: KProperty<*>,
        range: OpenEndRange<V>,
        function: F? = null
    ): T where V : Number, V : Comparable<V> {
        val columnBean = ColumnBean.byProperty(property)
        between(columnBean.columnName, range.start, range.endExclusive, function, columnBean.tableName)
        return thisT
    }

    open fun notBetween(
        column: String,
        start: Number,
        end: Number,
        function: F? = null,
        tableName: String? = null
    ): T {
        addSegment(column, SqlKeyword.NOT_BETWEEN, start to end, function, tableName)
        return thisT
    }

    open fun or(consumer: T.() -> Unit): T {
        addNestedCondition(SqlKeyword.OR, consumer)
        return thisT
    }

    open fun and(consumer: T.() -> Unit): T {
        addNestedCondition(SqlKeyword.AND, consumer)
        return thisT
    }

    protected fun addNestedCondition(connection: String, consumer: T.() -> Unit): T {
        addSegment(getCondition(values, tableClass, connection).apply(consumer).nestedSegment)
        return thisT
    }

    abstract fun getCondition(values: ArrayList<Any>, tableClass: KClass<*>, connection: String): T

    private fun addSegment(conditionSegment: ConditionSegment) {
        nestedSegment.addSegment(conditionSegment)
    }

    protected fun addSegment(
        columnName: String,
        sqlKey: String,
        value: Any?,
        function: F? = null,
        tableName: String? = null
    ) {
        addValue(value)
        nestedSegment.addSegment(columnName, sqlKey, value, function, tableName)
    }


    protected fun addValue(value: Any?) {
        when (value) {
            null, is KProperty<*> -> {
            }
            is String, is Char, is Number, is Boolean -> values.add(value)
            is QueryWrapper -> value.getSqlBindArgs().forEach { addValue(it) }
            is Array<*> -> value.forEach { addValue(it) }
            is Iterable<*> -> value.forEach { addValue(it) }
            is ByteArray -> values.addAll(value.asList())
            is CharArray -> values.addAll(value.asList())
            is ShortArray -> values.addAll(value.asList())
            is IntArray -> values.addAll(value.asList())
            is LongArray -> values.addAll(value.asList())
            is FloatArray -> values.addAll(value.asList())
            is DoubleArray -> values.addAll(value.asList())
            is BooleanArray -> values.addAll(value.asList())
            is Pair<*, *> -> {
                addValue(value.first)
                addValue(value.second)
            }

            else -> {
                throw IllegalStateException("不支持的数据类型")
            }
        }
    }

    internal fun isNotEmpty() = nestedSegment.isNotEmpty()

    override fun getSegment(isFormat: Boolean): String {
        return nestedSegment.getSegment(isFormat)
    }
}