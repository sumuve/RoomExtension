package com.cjj.re.keys


/**
 * sql函数定义
 */
interface SqlFunctions {
    val value: String
}

/**
 * where条件可用的函数
 */
interface SqlWhereFunctions : SqlFunctions

/**
 * 字符串函数 (数值函数)
 */
enum class StringFunctions(override val value: String) : SqlWhereFunctions {
    /**
     * 将字符串转换为大写
     */
    UPPER("UPPER"),

    /**
     * 将字符串转换为小写
     */
    LOWER("LOWER"),

    /**
     * 去除字符串首尾的空格
     */
    TRIM("TRIM"),

    /**
     * 返回字符串的长度
     */
    LENGTH("LENGTH"),
}

/**
 * Numeric Functions (数值函数)
 */
enum class NumericFunctions(override val value: String) : SqlWhereFunctions {
    /**
     * 对数值进行四舍五入
     */
    ROUND("ROUND"),

    /**
     * 向上取整
     */
    CEIL("CEIL"),

    /**
     * 向下取整
     */
    FLOOR("FLOOR"),

    /**
     * 返回数值的绝对值
     */
    ABS("ABS"),


    /**
     * 返回数值的平方根
     */
    SQRT("SQRT"),
}

/**
 * Aggregate Functions (聚合函数)
 */
enum class AggregateFunctions(override val value: String) : SqlFunctions {
    /**
     * 返回匹配指定条件的行数
     */
    COUNT("COUNT"),

    /**
     * 返回数值列的总和
     */
    SUM("SUM"),

    /**
     * 返回数值列的平均值
     */
    AVG("AVG"),

    /**
     * 返回数值列的最小值
     */
    MIN("MIN"),

    /**
     * 返回数值列的最大值
     */
    MAX("MAX"),
}
