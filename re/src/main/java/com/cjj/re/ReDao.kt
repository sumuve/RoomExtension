package com.cjj.re

import com.cjj.re.aggregate.ReAggregateType
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
    inline fun <reified T> insert(vararg args: T): LongArray = ReUtil.timeLog({ "insert count" }) {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
        dao.insert(*args)
    }

    /**
     * 批量插入
     *
     * @return 行ID数组
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T> insert(args: Collection<T>): LongArray = ReUtil.timeLog({ "insert count" }) {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
        dao.insert(args)
    }

    /**
     * 批量更新
     *
     * @return 更新的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T> update(vararg args: T): Int = ReUtil.timeLog({ "update count" }) {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
        dao.update(*args)
    }

    /**
     * 批量更新
     *
     * @return 更新的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T> update(args: Collection<T>): Int = ReUtil.timeLog({ "update count" }) {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
        dao.update(args)
    }

    /**
     * 批量删除
     *
     * @return 删除的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T> delete(vararg args: T): Int = ReUtil.timeLog({ "delete count" }) {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
        dao.delete(*args)
    }

    /**
     * 批量删除
     *
     * @return 删除的条目数量
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T> delete(args: Collection<T>): Int = ReUtil.timeLog({ "delete count" }) {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
        dao.delete(args)
    }

    /**
     * 聚合函数查询
     *
     * @throws ClassNotFoundException entity不是@Entity修饰的类,或者ksp没有生成对应的模板代码
     */
    inline fun <reified T> aggregate(): ReAggregateType<T> {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
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
    inline fun <reified T> count(noinline wrapper: (AggregateWrapper.() -> Unit)? = null): Long {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
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
    inline fun <reified T> refresh(entity: T): Boolean = ReUtil.timeLog({ "refresh" }) {
        val dao = ReUtil
            .getDao<T>(T::class.simpleName)
            ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
        dao.refresh(entity)
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
    inline fun <reified T> query(join: Boolean = false, noinline wrapper: QueryWrapper.() -> Unit): List<T> =
        ReUtil.timeLog({ "query count" }) {
            val dao = ReUtil
                .getDao<T>(T::class.simpleName)
                ?: throw ClassNotFoundException(T::class.simpleName + "找不到对应的Dao")
            dao.query(join, wrapper)
        }


}