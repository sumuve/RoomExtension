package com.cjj.re.compiler.template

import com.cjj.re.compiler.bean.ClassBean
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

class ReCoreDaoClassTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile,
    private val classBean: ClassBean
) : ClassTemplate(env, kFile) {
    private val sourceClassName: String = classBean.className
    override val className: String = "__Re${sourceClassName}CoreDao"
    override val packageName: String = "com.cjj.re.dao.core"

    override fun getImportList(): List<String> = arrayListOf(
        "androidx.room.*",
        "androidx.sqlite.db.SupportSQLiteQuery",
        "com.cjj.re.base.ReBaseCoreDao",
        classBean.qualifiedName!!,
    )

    override fun getClassTemplate(sb: StringBuilder) {
        sb.append("@Dao\n")
        sb.append("interface $className :ReBaseCoreDao<${sourceClassName}> {\n")
        sb.append("    @Insert\n")
        sb.append("    override fun insert(vararg values: ${classBean.className}): LongArray\n\n")

        sb.append("    @Insert\n")
        sb.append("    override fun insert(values: Collection<${classBean.className}>): LongArray\n\n")


        sb.append("    @Update\n")
        sb.append("    override fun update(vararg values: ${classBean.className}): Int\n\n")

        sb.append("    @Update\n")
        sb.append("    override fun update(values: Collection<${classBean.className}>): Int\n\n")


        sb.append("    @Delete\n")
        sb.append("    override fun delete(vararg values: ${classBean.className}): Int\n\n")

        sb.append("    @Delete\n")
        sb.append("    override fun delete(values: Collection<${classBean.className}>): Int\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    override fun getNumberByInt(sql: SupportSQLiteQuery): Int\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    override fun getNumberByLong(sql: SupportSQLiteQuery): Long\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    override fun getNumberByFloat(sql: SupportSQLiteQuery): Float\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    override fun getNumberByDouble(sql: SupportSQLiteQuery): Double\n\n")


        sb.append("    @RawQuery\n")
        sb.append("    override fun getGroupByInt(sql: SupportSQLiteQuery): Map<${classBean.className}, @MapColumn(columnName = \"__group\") Int>\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    override fun getGroupByLong(sql: SupportSQLiteQuery): Map<${classBean.className}, @MapColumn(columnName = \"__group\") Long>\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    override fun getGroupByFloat(sql: SupportSQLiteQuery): Map<${classBean.className}, @MapColumn(columnName = \"__group\") Float>\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    override fun getGroupByDouble(sql: SupportSQLiteQuery): Map<${classBean.className}, @MapColumn(columnName = \"__group\") Double>\n\n")

        sb.append("    @RawQuery\n")
        sb.append("    @Transaction\n")
        sb.append("    override fun getList(sql: SupportSQLiteQuery): List<${classBean.className}>\n\n")


        if (classBean.primaryKeys.isNotEmpty()) {
            sb.append("    @Query(\"select * from ${classBean.tableName} where ${classBean.primaryKeys.joinToString(" and ") { field -> "${field.propertyValue} = :${field.propertyValue}" }}\") \n")
            sb.append("    @Transaction\n")
            sb.append("    fun getByPrimaryKey(${classBean.primaryKeys.joinToString(", ") { field -> "${field.propertyValue} :${field.type}" }}): ${classBean.className} \n\n")
        }
        sb.append("}")
    }

}
