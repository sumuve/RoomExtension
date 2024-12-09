package com.cjj.re.compiler.template

import com.cjj.re.compiler.bean.ClassBean
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

/**
 * <p>
 * class信息生成
 * </p>
 *
 * @author CJJ
 * @since 2024-08-05 15:04
 */
class ReTableInfoTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile,
    private val classBean: ClassBean
) :
    ClassTemplate(env, kFile) {
    private val sourceClassName: String = classBean.className
    override val className: String = "__Re${classBean.className}TableInfo"
    override val packageName: String = "com.cjj.re.tableinfo"

    override fun getImportList(): List<String> = arrayListOf("com.cjj.re.base.ReTableInfo")

    override fun getClassTemplate(classSb: StringBuilder) {
        classSb.append("object $className : ReTableInfo {\n\n");
        classSb.append("    override val className = \"${sourceClassName}\"\n\n")
        classSb.append("    override val tableName = \"${classBean.tableName}\"\n\n")
        classSb.append("    private val columnMap by lazy {\n")
        classSb.append("        hashMapOf(\n")
        classBean.propertyList.forEach { property ->
            if (!property.isIgnore()) {
                classSb.append("            \"${property.propertyName}\" to \"${property.propertyValue}\",\n")
            }
        }
        classSb.append("        )\n")
        classSb.append("    }\n\n")
        classSb.append("    override fun columnName(propertiesName: String): String {\n")
        classSb.append("        return columnMap[propertiesName] ?: throw IllegalStateException(\n")
        classSb.append("            String.format(\n")
        classSb.append("                \"%s表中找不到%s字段\",\n")
        classSb.append("                tableName,\n")
        classSb.append("                propertiesName\n")
        classSb.append("            )\n")
        classSb.append("        )\n")
        classSb.append("    }\n")
        classSb.append("}")
    }
}