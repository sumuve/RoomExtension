package com.cjj.re.condition

import com.cjj.re.keys.SqlWhereFunctions
import kotlin.reflect.KClass

class WhereConditionController internal constructor(
    values: ArrayList<Any>,
    tableClass: KClass<*>,
    connection: String = ""
) : ConditionController<WhereConditionController, SqlWhereFunctions>(values, tableClass, connection) {

    override fun getCondition(
        values: ArrayList<Any>,
        tableClass: KClass<*>,
        connection: String
    ): WhereConditionController = WhereConditionController(values, tableClass, connection)

}