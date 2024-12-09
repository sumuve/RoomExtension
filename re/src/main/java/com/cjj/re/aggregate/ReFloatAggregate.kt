package com.cjj.re.aggregate

import com.cjj.re.keys.AggregateFunctions
import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.GroupByAggregateWrapper
import kotlin.reflect.KProperty

class ReFloatAggregate<T>(private val aggregate: ReAggregate<T>) : ReNumberAggregate<T, Float> {

    override fun sum(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Float {
        return aggregate.aggregateFloat(AggregateFunctions.SUM, column, wrapper)
    }


    override fun avg(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Float {
        return aggregate.aggregateFloat(AggregateFunctions.AVG, column, wrapper)
    }

    override fun max(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Float {
        return aggregate.aggregateFloat(AggregateFunctions.MAX, column, wrapper)
    }

    override fun min(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Float {
        return aggregate.aggregateFloat(AggregateFunctions.MIN, column, wrapper)
    }


    override fun groupBySum(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Float> {
        return aggregate.aggregateGroupByFloat(AggregateFunctions.SUM, column, wrapper)
    }

    override fun groupByAvg(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Float> {
        return aggregate.aggregateGroupByFloat(AggregateFunctions.AVG, column, wrapper)
    }

    override fun groupByMax(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Float> {
        return aggregate.aggregateGroupByFloat(AggregateFunctions.MAX, column, wrapper)
    }

    override fun groupByMin(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Float> {
        return aggregate.aggregateGroupByFloat(AggregateFunctions.MIN, column, wrapper)
    }
}
