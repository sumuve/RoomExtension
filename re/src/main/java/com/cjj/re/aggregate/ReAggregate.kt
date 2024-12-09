package com.cjj.re.aggregate

import com.cjj.re.base.ReBaseCoreDao
import com.cjj.re.keys.AggregateFunctions
import com.cjj.re.util.ReUtil
import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.GroupByAggregateWrapper
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ReAggregate<T>(private val dao: ReBaseCoreDao<T>, private val kClass: KClass<*>) {


    fun aggregateInt(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>? = null,
        wrapper: (AggregateWrapper.() -> Unit)? = null
    ): Int = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = AggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getNumberByInt(ReUtil.getSql(aggregateWrapper))
        result
    }

    fun aggregateLong(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>? = null,
        wrapper: (AggregateWrapper.() -> Unit)? = null
    ): Long = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = AggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getNumberByLong(ReUtil.getSql(aggregateWrapper))
        result
    }

    fun aggregateFloat(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>,
        wrapper: (AggregateWrapper.() -> Unit)? = null
    ): Float = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = AggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getNumberByFloat(ReUtil.getSql(aggregateWrapper))
        result
    }

    fun aggregateDouble(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>,
        wrapper: (AggregateWrapper.() -> Unit)? = null
    ): Double = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = AggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getNumberByDouble(ReUtil.getSql(aggregateWrapper))
        result
    }

    fun aggregateGroupByInt(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>,
        wrapper: (GroupByAggregateWrapper.() -> Unit)? = null
    ): Map<T, Int> = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = GroupByAggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getGroupByInt(ReUtil.getSql(aggregateWrapper))
        result
    }

    fun aggregateGroupByLong(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>,
        wrapper: (GroupByAggregateWrapper.() -> Unit)? = null
    ): Map<T, Long> = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = GroupByAggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getGroupByLong(ReUtil.getSql(aggregateWrapper))
        result
    }

    fun aggregateGroupByFloat(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>,
        wrapper: (GroupByAggregateWrapper.() -> Unit)? = null
    ): Map<T, Float> = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = GroupByAggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getGroupByFloat(ReUtil.getSql(aggregateWrapper))
        result
    }

    fun aggregateGroupByDouble(
        sqlFunction: AggregateFunctions,
        column: KProperty<*>,
        wrapper: (GroupByAggregateWrapper.() -> Unit)? = null
    ): Map<T, Double> = ReUtil.timeLog({ "query ${sqlFunction.value.lowercase()}" }) {
        val aggregateWrapper = GroupByAggregateWrapper(sqlFunction, column, kClass)
        wrapper?.invoke(aggregateWrapper)
        val result = dao.getGroupByDouble(ReUtil.getSql(aggregateWrapper))
        result
    }

}
