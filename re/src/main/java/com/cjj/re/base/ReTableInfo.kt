package com.cjj.re.base

/**
 * <p>
 * 成员信息接口
 * </p>
 *
 * @author CJJ
 * @since 2024-08-05 14:35
 */
interface ReTableInfo {
    /**
     * 实体类名
     */
    val className:String
    /**
     * 表名
     */
    val tableName:String

    /**
     * 列名获取
     */
    fun columnName(propertiesName:String):String

}