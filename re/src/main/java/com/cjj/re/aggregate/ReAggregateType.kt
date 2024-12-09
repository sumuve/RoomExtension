package com.cjj.re.aggregate

class ReAggregateType<T>(private val aggregate: ReAggregate<T>) {

    private val aggregateInt by lazy {
        ReIntAggregate(aggregate)
    }

    private val aggregateLong by lazy {
        ReLongAggregate(aggregate)
    }

    private val aggregateFloat by lazy {
        ReFloatAggregate(aggregate)
    }

    private val aggregateDouble by lazy {
        ReDoubleAggregate(aggregate)
    }

    /**
     * Int类型聚合函数查询
     */
    fun int() = aggregateInt

    /**
     * Long类型聚合函数查询
     */
    fun long() = aggregateLong

    /**
     * Float类型聚合函数查询
     */
    fun float() = aggregateFloat

    /**
     * Double类型聚合函数查询
     */
    fun double() = aggregateDouble

}
