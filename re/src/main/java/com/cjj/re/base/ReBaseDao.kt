package com.cjj.re.base

import android.util.Log
import com.cjj.re.ReManager
import com.cjj.re.aggregate.ReAggregate
import com.cjj.re.aggregate.ReAggregateType
import com.cjj.re.util.RE_TAG
import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.QueryWrapper
import kotlin.math.log

/**
 * 数据操作基类
 */
abstract class ReBaseDao<T> {

    /**
     * dao
     */
    protected abstract val dao: ReBaseCoreDao<T>

    /**
     * 聚合函数
     */
    protected abstract val aggregate: ReAggregate<T>

    private val aggregateType by lazy {
        ReAggregateType(aggregate)
    }

    fun insert(vararg args: T): LongArray {
        return dao.insert(*args)
    }

    fun insert(args: Collection<T>): LongArray {
        return dao.insert(args)
    }

    fun update(vararg args: T): Int {
        return dao.update(*args)
    }

    fun update(args: Collection<T>): Int {
        return dao.update(args)
    }

    fun delete(vararg args: T): Int {
        return dao.delete(*args)
    }

    fun delete(args: Collection<T>): Int {
        return dao.delete(args)
    }

    fun aggregate() = aggregateType

    fun count(wrapper: (AggregateWrapper.() -> Unit)? = null): Long {
        return aggregateType.long().count(wrapper)
    }

    abstract fun query(relevance: Boolean = false, wrapper: QueryWrapper.() -> Unit): List<T>

    abstract fun refresh(entity: T): Boolean
}
