package com.cjj.re.aggregate

import com.cjj.re.keys.AggregateFunctions
import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.GroupByAggregateWrapper
import kotlin.reflect.KProperty

class ReLongAggregate<T>(private val aggregate: ReAggregate<T>) : ReNumberAggregate<T, Long> {

    fun count(wrapper: (AggregateWrapper.() -> Unit)?): Long {
        return aggregate.aggregateLong(AggregateFunctions.COUNT, null, wrapper)
    }

    override fun sum(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Long {
        return aggregate.aggregateLong(AggregateFunctions.SUM, column, wrapper)
    }


    override fun avg(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Long {
        return aggregate.aggregateLong(AggregateFunctions.AVG, column, wrapper)
    }

    override fun max(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Long {
        return aggregate.aggregateLong(AggregateFunctions.MAX, column, wrapper)
    }

    override fun min(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Long {
        return aggregate.aggregateLong(AggregateFunctions.MIN, column, wrapper)
    }


    fun groupByCount(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Long> {
        return aggregate.aggregateGroupByLong(AggregateFunctions.COUNT, column, wrapper)
    }

    override fun groupBySum(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Long> {
        return aggregate.aggregateGroupByLong(AggregateFunctions.SUM, column, wrapper)
    }

    override fun groupByAvg(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Long> {
        return aggregate.aggregateGroupByLong(AggregateFunctions.AVG, column, wrapper)
    }

    override fun groupByMax(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Long> {
        return aggregate.aggregateGroupByLong(AggregateFunctions.MAX, column, wrapper)
    }

    override fun groupByMin(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Long> {
        return aggregate.aggregateGroupByLong(AggregateFunctions.MIN, column, wrapper)
    }
}
