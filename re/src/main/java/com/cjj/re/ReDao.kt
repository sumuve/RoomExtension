package com.cjj.re

import com.cjj.re.aggregate.ReAggregateType
import com.cjj.re.util.InterceptorUtil
import com.cjj.re.util.ObserverUtil
import com.cjj.re.util.ReUtil
import com.cjj.re.wrapper.AggregateWrapper
import com.cjj.re.wrapper.QueryWrapper

/**
 * <p>
 * ReDao 数据操作类
 * </p>
 *
 * @author CJJ
 * @since 2024-08-05 09:51
 */
object ReDao {

    /**
     * 批量插入到数据库
     *
     * @return 行ID数组
     */
    inline fun <reified T : Any> insert(vararg args: T): LongArray =
        insert(args.toList())

    /**
     * 批量插入
     *
     * @return 行ID数组
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> insert(args: Collection<T>): LongArray =
        ReUtil.timeLog({ "${T::class.simpleName} insert count" }) {
            val dao = ReUtil.getDao<T>(T::class)
            val newData = InterceptorUtil.interceptorInsert(T::class, args)
            if (newData.isEmpty()) {
                return@timeLog longArrayOf()
            }
            dao.insert(newData).also {
                ObserverUtil.noticeInsert(T::class, newData)
            }
        }

    /**
     * 批量更新
     *
     * @return 更新的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> update(vararg args: T): Int =
        update(args.toList())

    /**
     * 批量更新
     *
     * @return 更新的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> update(args: Collection<T>): Int =
        ReUtil.timeLog({ "${T::class.simpleName} update count" }) {
            val dao = ReUtil.getDao<T>(T::class)
            val newData = InterceptorUtil.interceptorUpdate(T::class, args)
            if (newData.isEmpty()) {
                return@timeLog 0
            }
            dao.update(newData).also {
                ObserverUtil.noticeUpdate(T::class, newData)
            }
        }

    /**
     * 批量删除
     *
     * @return 删除的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> delete(vararg args: T): Int =
        delete(args.toList())

    /**
     * 批量删除
     *
     * @return 删除的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> delete(args: Collection<T>): Int =
        ReUtil.timeLog({ "${T::class.simpleName} delete count" }) {
            val dao = ReUtil.getDao<T>(T::class)
            val newData = InterceptorUtil.interceptorDelete(T::class, args)
            if (newData.isEmpty()) {
                return@timeLog 0
            }
            dao.delete(newData).also {
                ObserverUtil.noticeDelete(T::class, newData)
            }
        }

    /**
     * 聚合函数查询
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> aggregate(): ReAggregateType<T> {
        val dao = ReUtil.getDao<T>(T::class)
        return dao.aggregate()
    }

    /**
     *
     * 查询满足条件的条目数量
     *
     * @param wrapper 查询条件,为空则查询该表所有条目数量
     *
     * @return 条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> count(noinline wrapper: (AggregateWrapper.() -> Unit)? = null): Long {
        val dao = ReUtil.getDao<T>(T::class)
        return dao.count(wrapper)
    }


    /**
     * 通过primaryKey从数据库中查询最新数据刷新实例
     * @param entity 实例
     *
     * @return `true`:查询到数据并刷新了数据 `false`:数据查不到这条数据
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> refresh(entity: T): Boolean = ReUtil.timeLog({ "${T::class.simpleName} refresh" }) {
        val dao = ReUtil.getDao<T>(T::class)
        val newEntity = InterceptorUtil.interceptorRefresh(T::class, entity) ?: return@timeLog false
        dao.refresh(newEntity).also {
            ObserverUtil.noticeRefresh(T::class, newEntity)
        }
    }


    /**
     * 查询数据库
     *
     * @param join 是否关联查询
     * @param wrapper 查询条件
     *
     * @return 数据列表
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T : Any> query(join: Boolean = false, noinline wrapper: QueryWrapper.() -> Unit): List<T> =
        ReUtil.timeLog({ "${T::class.simpleName} query count" }) {
            val dao = ReUtil.getDao<T>(T::class)
            dao.query(join, wrapper)
        }


}