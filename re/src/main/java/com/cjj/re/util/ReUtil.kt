package com.cjj.re.util

import android.os.Looper
import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.cjj.re.ReManager
import com.cjj.re.base.ReBaseDao
import com.cjj.re.wrapper.Wrapper
import kotlin.reflect.KClass

/**
 *
 *
 * Dao获取管理器
 *
 *
 * @author CJJ
 * @since 2024-08-05 09:56
 */
internal const val RE_TAG = "ReTag"

object ReUtil {

    private val daoMap = HashMap<KClass<*>, ReBaseDao<*>?>()

    /** @noinspection unchecked
     * 获取Dao
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getDao(entityClass: KClass<*>): ReBaseDao<T> {
        val className = entityClass.simpleName ?: throw ClassNotFoundException("className为空")
        var reBaseDao = daoMap[entityClass]
        if (reBaseDao == null) {
            reBaseDao =
                ObjectReflectUtil.getInstance<ReBaseDao<T>>(String.format("com.cjj.re.dao.__Re%sDao", className))
            daoMap[entityClass] = reBaseDao
        }
        return reBaseDao as? ReBaseDao<T> ?: throw ClassNotFoundException("${className}找不到对应的Dao")
    }

    /**
     * 统计时间并打印日志
     */
    fun <T> timeLog(operation: () -> String, function: () -> T): T {
        if (!ReManager.isLog) {
            return function()
        }

        val oldTime = System.currentTimeMillis()
        val result = function()
        val nowTime = System.currentTimeMillis()
        when (result) {
            is Collection<*> -> {
                Log.i(RE_TAG, "${operation()}:${result.size}  time:${nowTime - oldTime}ms")
            }

            is Map<*, *> -> {
                Log.i(RE_TAG, "${operation()}:${result.size}  time:${nowTime - oldTime}ms")
            }

            is LongArray -> {
                Log.i(RE_TAG, "${operation()}:${result.size}  time:${nowTime - oldTime}ms")
            }

            is Number -> {
                Log.i(RE_TAG, "${operation()}:${result}  time:${nowTime - oldTime}ms")
            }

            else -> {
                Log.i(RE_TAG, "${operation()}  time:${nowTime - oldTime}ms")
            }
        }
        return result
    }

    /**
     * 获取Sql,并判断是否需要打印日志
     */
    fun getSql(wrapper: Wrapper<*>): SimpleSQLiteQuery {
        val args = wrapper.getSqlBindArgs()
        val sql = wrapper.build(false)
        if (ReManager.isLog) {
            if (args.isNotEmpty()) {
                if (ReManager.isSqlFormat) {
                    val formatSql = wrapper.build(true)
                    Log.i(RE_TAG, formatSql)
                } else {
                    Log.i(RE_TAG, sql)
                    Log.i(RE_TAG, "[${args.joinToString(",")}]")
                }
            } else {
                Log.i(RE_TAG, sql)
            }
        }
        return SimpleSQLiteQuery(sql, args.toTypedArray())
    }

    fun postMainThread(black: () -> Unit) {
        if (Looper.getMainLooper().thread == Thread.currentThread()) {
            black.invoke()
        } else {
            ReManager.mainHandler.post(black)
        }
    }
}
