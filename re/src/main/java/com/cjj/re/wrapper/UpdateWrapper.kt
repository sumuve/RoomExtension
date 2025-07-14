package com.cjj.re.wrapper

import com.cjj.re.keys.SqlKeyword
import com.cjj.re.segment.ConditionSegment
import com.cjj.re.segment.NormalSegment
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

    override fun build(isFormat: Boolean): String {
        val sb = StringBuilder()
        sb.append(SqlKeyword.UPDATE)
        sb.append(" ")
        sb.append(tableName)
        if (setSegment.isNotEmpty()) {
            sb.append(" ")
            sb.append(SqlKeyword.SET)
            sb.append(" ")
            sb.append(ConditionSegment.getSegment(setSegment, isFormat))
        }
        sb.append(super.build(isFormat))
        return sb.toString()
    }

    override fun getSqlBindArgs(): List<Any> = arrayListOf()
}