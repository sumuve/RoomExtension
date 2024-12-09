package com.cjj.re.aggregate

import com.cjj.re.keys.AggregateFunctions
import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.GroupByAggregateWrapper
import kotlin.reflect.KProperty

class ReDoubleAggregate<T>(private val aggregate: ReAggregate<T>) : ReNumberAggregate<T, Double> {

    override fun sum(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Double {
        return aggregate.aggregateDouble(AggregateFunctions.SUM, column, wrapper)
    }


    override fun avg(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Double {
        return aggregate.aggregateDouble(AggregateFunctions.AVG, column, wrapper)
    }

    override fun max(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Double {
        return aggregate.aggregateDouble(AggregateFunctions.MAX, column, wrapper)
    }

    override fun min(column: KProperty<*>, wrapper: (AggregateWrapper.() -> Unit)?): Double {
        return aggregate.aggregateDouble(AggregateFunctions.MIN, column, wrapper)
    }

    override fun groupBySum(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Double> {
        return aggregate.aggregateGroupByDouble(AggregateFunctions.SUM, column, wrapper)
    }

    override fun groupByAvg(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Double> {
        return aggregate.aggregateGroupByDouble(AggregateFunctions.AVG, column, wrapper)
    }

    override fun groupByMax(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Double> {
        return aggregate.aggregateGroupByDouble(AggregateFunctions.MAX, column, wrapper)
    }

    override fun groupByMin(column: KProperty<*>, wrapper: (GroupByAggregateWrapper.() -> Unit)?): Map<T, Double> {
        return aggregate.aggregateGroupByDouble(AggregateFunctions.MIN, column, wrapper)
    }
}
