package com.cjj.re.compiler.template

import com.cjj.re.compiler.bean.ClassBean
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

class DataBaseClassTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile,
    private val classBean: ClassBean
) :
    ClassTemplate(env, kFile) {
    private val sourceClassName: String = classBean.className
    override val className: String = "__Re${sourceClassName}DataBase"
    override val packageName: String = "com.cjj.re.dataBase"
    override fun getImportList(): List<String> = arrayListOf(
        "androidx.room.Database",
        "androidx.room.Entity",
        "androidx.room.RenameColumn",
        "androidx.room.RoomDatabase",
        "com.cjj.re.dao.core.__Re${sourceClassName}CoreDao",
        classBean.qualifiedName!!,
    )

    override fun getClassTemplate(sb: StringBuilder) {
        sb.append(
            """
                @Database(
                    entities = [
                        ${sourceClassName}::class,
                    ],
                    version = 1,
                    exportSchema = false
                )
                abstract class Re${sourceClassName}DataBase : RoomDatabase() {
                    abstract fun get__Re${sourceClassName}CoreDao(): __Re${sourceClassName}CoreDao
                }       
        """.trimIndent()
        )
    }
}
