package com.cjj.re.aggregate

import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.GroupByAggregateWrapper
import kotlin.reflect.KProperty

interface ReNumberAggregate<T, R> {
    /**
     * 求和函数
     */
    fun sum(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)? = null): R

    /**
     * 平均数函数
     */
    fun avg(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)? = null): R

    /**
     * 最大值函数
     */
    fun max(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)? = null): R

    /**
     * 最小值函数
     */
    fun min(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)? = null): R

    /**
     * 分组求和函数
     */
    fun groupBySum(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)? = null): Map<T, R>

    /**
     * 分组平均数函数
     */
    fun groupByAvg(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)? = null): Map<T, R>

    /**
     * 分组最大值函数
     */
    fun groupByMax(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)? = null): Map<T, R>

    /**
     * 分组最小值函数
     */
    fun groupByMin(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)? = null): Map<T, R>
}
