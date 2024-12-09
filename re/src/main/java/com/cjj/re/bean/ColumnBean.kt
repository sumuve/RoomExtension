package com.cjj.re.bean

import com.cjj.re.util.TableUtils
import kotlin.reflect.KProperty

data class ColumnBean(val tableName: String, val columnName: String) {

    companion object {
        fun byProperty(property: KProperty<*>): ColumnBean {
            val ownerClassName = TableUtils.getOwnerClassName(property)
            val tableName = TableUtils.getTableName(ownerClassName)
            val columnName =
                TableUtils.getColumnNameByClassName(ownerClassName, property.name)
            return ColumnBean(tableName, columnName)
        }
    }
}
