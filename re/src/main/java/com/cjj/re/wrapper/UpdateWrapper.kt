package com.cjj.re

import com.cjj.re.keys.SqlKeyword
import com.cjj.re.segment.ConditionSegment
import com.cjj.re.segment.NormalSegment
import com.cjj.re.wrapper.Wrapper
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class UpdateWrapper(kClass: KClass<*>) : Wrapper<UpdateWrapper>(kClass) {

    override val alias: Boolean = false

    private val setSegment = arrayListOf<NormalSegment>()

    fun set(property1: KProperty<*>, value: Any) = apply {
        set(property1.name, value)
    }

    fun set(column: String, value: Any) = apply {
        checkValue(value)
        addSegment(column, value)
    }

    private fun checkValue(value: Any) {
        if (value !is Number && value !is String && value !is Char && value !is Boolean) {
            throw IllegalStateException("value只能是基本数据类型")
        }
    }

    private fun addSegment(columnName: String, value: Any?) {
        setSegment.add(NormalSegment(",", columnName, SqlKeyword.EQ, value))
    }

    override fun build(): String {
        val sb = StringBuilder()
        sb.append("UPDATE $tableName")
        if (setSegment.isNotEmpty()) {
            sb.append(" SET ")
            sb.append(ConditionSegment.getSegment(setSegment))
        }
        sb.append(super.build())
        return sb.toString()
    }

    override fun getSqlBindArgs(): List<Any> = arrayListOf()
}