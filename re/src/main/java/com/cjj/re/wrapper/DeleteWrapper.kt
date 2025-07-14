package com.cjj.re

import com.cjj.re.wrapper.Wrapper
import kotlin.reflect.KClass

class DeleteWrapper(kClass: KClass<*>) :
    Wrapper<DeleteWrapper>(kClass) {

    override val alias: Boolean = false

    override fun build(isFormat: Boolean): String {
        val sb = StringBuilder()
        sb.append("DELETE FROM $tableName")
        sb.append(super.build(isFormat))
        return sb.toString()
    }

    override fun getSqlBindArgs(): List<Any> = arrayListOf()
}