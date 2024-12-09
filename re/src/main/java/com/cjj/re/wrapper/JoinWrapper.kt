package com.cjj.re.wrapper

import com.cjj.re.condition.WhereConditionController
import com.cjj.re.util.TableUtils
import kotlin.reflect.KClass

class JoinWrapper(joinTableClass: KClass<*>) {
    val values = arrayListOf<Any>()
    val tableName: String = TableUtils.getTableName(joinTableClass)
    val controller: WhereConditionController = WhereConditionController(values, joinTableClass)


    fun condition(condition: (WhereConditionController.() -> Unit)): JoinWrapper {
        condition.invoke(controller)
        return this
    }

    fun build(): String {
        return if (controller.isNotEmpty()) {
            "join $tableName on ${controller.getSegment()}"
        } else {
            "join $tableName"
        }
    }
}