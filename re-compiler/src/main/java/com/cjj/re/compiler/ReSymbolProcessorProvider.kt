package com.cjj.re.compiler

import androidx.room.Entity
import com.cjj.re.compiler.env.Env
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

internal class ReSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ReSymbolProcessor(environment)
    }

}

internal class ReSymbolProcessor(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        Env._environment = environment
        val symbols = resolver.getSymbolsWithAnnotation(Entity::class.qualifiedName!!)
        val ret = mutableListOf<KSAnnotated>()
        val sou = mutableListOf<KSClassDeclaration>()
        symbols.toList().forEach {
//            if (!it.validate())
//                ret.add(it)
//            else
                if (it is KSClassDeclaration) {
                    sou.add(it)
                }
        }
        if (sou.isNotEmpty()) {
            ReCompiler(environment, sou).visitClassDeclaration()
        }
        Env._environment = null
        //返回无法处理的符号
        return ret
    }
}
