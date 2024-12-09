package com.cjj.re.compiler.env

import com.cjj.re.compiler.bean.ClassBean
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

object Env {
    private val clazzMap: HashMap<String, ClassBean> = hashMapOf()
    var _environment: SymbolProcessorEnvironment? = null
    val environment: SymbolProcessorEnvironment
        get() = _environment!!

    fun putClazz(clazzName: String, bean: ClassBean) {
        clazzMap[clazzName] = bean
    }

    fun getClazz(clazzName: String) = clazzMap[clazzName]

}