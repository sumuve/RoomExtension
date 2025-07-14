package com.cjj.re.segment

import com.cjj.re.keys.SqlFunctions
import com.cjj.re.keys.SqlKeyword

open class NestedSegment(override val condition: String) : ConditionSegment {
    protected val conditionList = arrayListOf<ConditionSegment>()


    fun addNestedCondition(type: String, consumer: (NestedSegment) -> Unit) = apply {
        val nestedSegment = NestedSegment(type)
        consumer.invoke(nestedSegment)
        conditionList.add(nestedSegment)
    }


    fun addSegment(conditionSegment: ConditionSegment) {
        conditionList.add(conditionSegment)
    }

    fun addSegment(
        columnName: String,
        sqlKey: String,
        value: Any?,
        function: SqlFunctions? = null,
        tableName: String? = null,
    ) {
        conditionList.add(NormalSegment(SqlKeyword.AND, columnName, sqlKey, value, function, tableName))
    }

    internal fun isNotEmpty() = conditionList.isNotEmpty()

    override fun getSegment(isFormat: Boolean): String {
        return ConditionSegment.getSegment(conditionList, isFormat)
    }
}