package com.cjj.re.compiler.template

import com.cjj.re.compiler.bean.ClassBean
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

class ReAggregateClassTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile,
    private val classBean: ClassBean
) :
    ClassTemplate(env, kFile) {
    private val sourceClassName: String = classBean.className
    override val className: String = "Re${classBean.className}Aggregate"
    override val packageName: String = "com.cjj.re.aggregate.table"

    override fun getImportList(): List<String> = arrayListOf(
        classBean.qualifiedName!!,
        "com.cjj.re.aggregate.ReBaseAggregate",
        "com.cjj.re.ReManager",
        "com.cjj.re.dao.core.Re${sourceClassName}CoreDao",
        "kotlin.reflect.KClass",
    )

    override fun getClassTemplate(sb: StringBuilder) {
        sb.append(
            """
                class Re${sourceClassName}Aggregate(dao: Re${sourceClassName}CoreDao) : ReBaseAggregate<${sourceClassName}>(dao) {
                    override val kClass: KClass<*> = ${sourceClassName}::class
                }
        """.trimIndent()
        )
    }
}
