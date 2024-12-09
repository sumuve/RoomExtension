package com.cjj.re.aggregate

import com.cjj.re.keys.AggregateFunctions
import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.GroupByAggregateWrapper
import kotlin.reflect.KProperty

class ReIntAggregate<T>(private val aggregate: ReAggregate<T>) : ReNumberAggregate<T, Int> {

    fun count(column: KProperty<*>?, wrapper: (AggregateWrapper.() -> Unit)?): Int {
        return aggregate.aggregateInt(AggregateFunctions.COUNT, column, wrapper)
    }

    override fun sum(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Int {
        return aggregate.aggregateInt(AggregateFunctions.SUM, column, wrapper)
    }


    override fun avg(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Int {
        return aggregate.aggregateInt(AggregateFunctions.AVG, column, wrapper)
    }

    override fun max(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Int {
        return aggregate.aggregateInt(AggregateFunctions.MAX, column, wrapper)
    }

    override fun min(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Int {
        return aggregate.aggregateInt(AggregateFunctions.MIN, column, wrapper)
    }


    fun groupByCount(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Int> {
        return aggregate.aggregateGroupByInt(AggregateFunctions.COUNT, column, wrapper)
    }

    override fun groupBySum(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Int> {
        return aggregate.aggregateGroupByInt(AggregateFunctions.SUM, column, wrapper)
    }

    override fun groupByAvg(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Int> {
        return aggregate.aggregateGroupByInt(AggregateFunctions.AVG, column, wrapper)
    }

    override fun groupByMax(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Int> {
        return aggregate.aggregateGroupByInt(AggregateFunctions.MAX, column, wrapper)
    }

    override fun groupByMin(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Int> {
        return aggregate.aggregateGroupByInt(AggregateFunctions.MIN, column, wrapper)
    }
}
