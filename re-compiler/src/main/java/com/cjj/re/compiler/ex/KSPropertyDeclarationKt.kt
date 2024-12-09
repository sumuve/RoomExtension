package com.cjj.re.compiler.ex

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import kotlin.reflect.KCallable
import kotlin.reflect.KClass


fun KSPropertyDeclaration.containsAnnotation(clazz: KClass<*>): Boolean {
    return containsAnnotation(clazz.simpleName)
}

fun KSPropertyDeclaration.containsAnnotation(simpleName: String?): Boolean {
    return this.annotations.any { it.shortName.asString() == simpleName }
}

fun KSPropertyDeclaration.findKSAnnotation(clazz: KClass<*>): KSAnnotation? {
    return findKSAnnotation(clazz.simpleName)
}

fun KSPropertyDeclaration.findKSAnnotation(simpleName: String?): KSAnnotation? {
    return this.annotations.find { it.shortName.asString() == simpleName }
}

fun KSAnnotation.findKSArgument(kCallable: KCallable<*>): KSValueArgument? {
    return findKSArgument(kCallable.name)
}

fun KSAnnotation.findKSArgument(name: String): KSValueArgument? {
    return arguments.find { it.name?.asString() == name }
}



