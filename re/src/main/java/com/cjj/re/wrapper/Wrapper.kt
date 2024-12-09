package com.cjj.re.wrapper

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.cjj.re.ReManager
import com.cjj.re.condition.WhereConditionController
import com.cjj.re.util.RE_TAG
import com.cjj.re.util.TableUtils
import kotlin.math.log
import kotlin.reflect.KClass

abstract class Wrapper<out T : Wrapper<T>>(
    tableClass: KClass<*>
) {

    protected abstract val alias: Boolean
    internal val values = arrayListOf<Any>()


    @Suppress("UNCHECKED_CAST", "LeakingThis")
    private val thisT = this as T

    /**
     * 当前查询的Dao ClassName
     */
    protected val tableClassName = tableClass.qualifiedName!!

    /**
     * 当前查询的表名
     */
    protected val tableName = TableUtils.getTableName(tableClass)

    private val tableSet = hashSetOf(tableName)

    private val controller by lazy { WhereConditionController(values, tableClass) }

    fun where(condition: (WhereConditionController.() -> Unit)): T {
        condition.invoke(controller)
        return thisT
    }

    private fun checkTable(tableName: String) {
        if (!tableSet.contains(tableName)) {
            throw IllegalStateException()
        }
    }

    open fun build(): String {
        val sb = StringBuilder()

        if (controller.isNotEmpty()) {
            sb.append(" ")
            sb.append("WHERE")
            sb.append(" ")
            sb.append(controller.getSegment())
        }

        return sb.toString()
    }

    abstract fun getSqlBindArgs(): List<Any>

}