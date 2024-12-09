package com.cjj.re.compiler.template

import androidx.room.Entity
import com.cjj.re.compiler.bean.ClassBean
import com.cjj.re.compiler.ex.findKSAnnotation
import com.cjj.re.compiler.ex.findKSArgument
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

class PropertiesClassTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile,
    private val classBeans: List<ClassBean>
) :
    ClassTemplate(env, kFile) {

    override val className: String = "Properties"
    override val packageName: String = "com.cjj.re.properties"

    override fun getImportList(): List<String> = arrayListOf()

    override fun getClassTemplate(sb: StringBuilder) {
        sb.append("object $className {\n")
        classBeans.forEach {

            val findKSAnnotation = it.kSClassDeclaration.findKSAnnotation(Entity::class)
            val findKSArgument = findKSAnnotation?.findKSArgument(Entity::tableName)
            sb.append("    private val ${it.tableName} by lazy {\n")
            sb.append("        hashMapOf(\n")
            it.propertyList.forEach { property ->
                if (!property.isIgnore()) {
                    sb.append("             \"${property.propertyName}\" to \"${property.propertyValue}\",\n")
                }
            }
            sb.append("        )\n")
            sb.append("    }\n\n")
        }
        sb.append("    private val tableInfo by lazy {\n")
        sb.append("        hashMapOf(\n")
        classBeans.forEach {
            sb.append("             \"${it.tableName}\" to ${it.tableName},\n")
        }
        sb.append("        )\n")
        sb.append("    }\n")


        sb.append("    private val tableName by lazy {\n")
        sb.append("        hashMapOf(\n")
        classBeans.forEach {
            sb.append("             \"${it.qualifiedName}\" to \"${it.tableName}\",\n")
        }
        sb.append("        )\n")
        sb.append("    }\n")

        sb.append(
            """
    fun getTableName(tableClassName: String): String {
        return tableName[tableClassName] ?: throw IllegalStateException(String.format("无法找到%s对应的表",tableClassName))
    }

    fun getTableInfo(tableName: String): HashMap<String,String> {
        return when (tableName) {
${
                classBeans.map {
                    String.format("            \"%s\" -> %s;", it.tableName, it.tableName)
                }.joinToString("\n")
            }
            else -> throw IllegalStateException("无法找到"+tableName+"表")
        }
    }

    fun getColumnNameByClassName(tableClassName: String, columnName: String): String {
        val tableName = getTableName(tableClassName)
        val tableInfo = getTableInfo(tableName)
        return tableInfo[columnName] ?: throw IllegalStateException(String.format("%s表中找不到%s字段",tableName,columnName))
    }

    fun getColumnNameByTableName(tableName: String, columnName: String): String {
        val tableInfo = getTableInfo(tableName)
        return tableInfo[columnName] ?: throw IllegalStateException(String.format("%s表中找不到%s",tableName,columnName))
    }
"""
        )

        sb.append("}\n")
    }
}