package com.cjj.re.util

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.cjj.re.ReManager
import com.cjj.re.base.ReBaseDao
import com.cjj.re.wrapper.Wrapper

/**
 *
 *
 * Dao获取管理器
 *
 *
 * @author CJJ
 * @since 2024-08-05 09:56
 */
const val RE_TAG = "ReTag"

object ReUtil {

    private val daoMap = HashMap<String, ReBaseDao<*>?>()

    /** @noinspection unchecked
     * 获取Dao
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getDao(className: String?): ReBaseDao<T>? {
        if (className == null) return null
        var reBaseDao = daoMap[className]
        if (reBaseDao == null) {
            reBaseDao =
                ObjectReflectUtil.getInstance<ReBaseDao<T>>(String.format("com.cjj.re.dao.__Re%sDao", className))
            daoMap[className] = reBaseDao
        }
        return reBaseDao as ReBaseDao<T>
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
        val sql = wrapper.build()
        if (ReManager.isLog) {
            if (ReManager.isSqlFormat) {
                if (args.isNotEmpty()) {
                    val formatSql = sql.replace("?", "%s").format(*args.map { it.toString() }.toTypedArray())
                    Log.i(RE_TAG, formatSql)
                } else {
                    Log.i(RE_TAG, sql)
                }
            } else {
                Log.i(RE_TAG, sql)
                if (args.isNotEmpty()) {
                    Log.i(RE_TAG, "[${args.joinToString(",")}]")
                }
            }
        }
        return SimpleSQLiteQuery(sql, args.toTypedArray())
    }
}
