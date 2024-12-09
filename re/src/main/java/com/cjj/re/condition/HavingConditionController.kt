package com.cjj.re.condition

import com.cjj.re.keys.SqlFunctions
import kotlin.reflect.KClass

class HavingConditionController internal constructor(
    values: ArrayList<Any>,
    tableClass: KClass<*>,
    connection: String = ""
) : ConditionController<HavingConditionController, SqlFunctions>(values, tableClass, connection) {

    override fun getCondition(
        values: ArrayList<Any>,
        tableClass: KClass<*>,
        connection: String
    ): HavingConditionController = HavingConditionController(values, tableClass, connection)

}