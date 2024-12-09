package com.cjj.re.util

import com.cjj.re.base.ReTableInfo
import kotlin.jvm.internal.PropertyReference
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object TableUtils {

    private val tableMap= hashMapOf<String,ReTableInfo>()

    /**
     * 获取字段所属父类名
     * @param property 字段
     */
    fun getOwnerClassName(property: KProperty<*>): String {
        if (property is PropertyReference) {
            val owner = property.owner
            if (owner is kotlin.jvm.internal.ClassReference) {
                return owner.simpleName ?: throw IllegalStateException()
            }
        }
        throw IllegalStateException()
    }

    /**
     * 获取字段对应的列名
     */
    fun getColumnName(property: KProperty<*>): String {
        val ownerClassName = getOwnerClassName(property)
        return getColumnNameByClassName(ownerClassName, property.name)
    }

    /**
     * 通过Dao类名获取表名
     */
    fun getTableName(tableClass: KClass<*>): String {
        return getTableName(tableClass.simpleName!!)
    }

    /**
     * 通过Dao类名获取表名
     */
    fun getTableName(tableClassName: String): String {
        return getTableInfo(tableClassName).tableName
    }

    /**
     * 通过Dao类名获取表名
     */
    fun getTableName(property: KProperty<*>): String {
        val ownerClassName = getOwnerClassName(property)
        return getTableName(ownerClassName)
    }

    /**
     * 通过Dao类名和字段名获取列表名称
     */
    fun getColumnNameByClassName(tableClassName: String, columnName: String): String {
        return getTableInfo(tableClassName).columnName(columnName)
    }

    private fun getTableInfo(className:String):ReTableInfo{
        return tableMap.getOrPut(className){
            ObjectReflectUtil.getInstance<ReTableInfo>("com.cjj.re.tableinfo.__Re${className}TableInfo")
                ?:throw throw IllegalStateException("无法找到${className}表")
        }
    }

}