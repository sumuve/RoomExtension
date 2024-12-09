package com.cjj.re.compiler.template

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

class BaseDaoClassTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile
) :
    ClassTemplate(env, kFile) {
    override val className: String = "ReBaseDao"
    override val packageName: String = "com.cjj.re.base"

    override fun getImportList(): List<String> = arrayListOf(
        "com.cjj.re.wrapper.QueryWrapper",
        "com.cjj.re.aggregate.*",
        "com.cjj.re.wrapper.AggregateWrapper",
        "kotlin.reflect.KProperty",
    )

    override fun getClassTemplate(sb: StringBuilder) {
        sb.append(
            """
            interface ReBaseDao<T> {
                fun insert(vararg args: T): LongArray

                fun insert(args: Collection<T>): LongArray

                fun update(vararg args: T): Int

                fun update(args: Collection<T>): Int

                fun delete(vararg args: T): Int

                fun delete(args: Collection<T>): Int

                fun aggregateInt(): ReIntAggregate<T>
                
                fun aggregateLong(): ReLongAggregate<T>
                
                fun aggregateFloat(): ReFloatAggregate<T>
                
                fun aggregateDouble(): ReDoubleAggregate<T>
    
                fun count(wrapper: (AggregateWrapper.() -> Unit)? = null): Long
                
                fun query(relevance: Boolean = false, wrapper: QueryWrapper.() -> Unit): List<T>
                
                fun refresh(entity: T): Boolean
            }
        """.trimIndent()
        )
    }
}
