package com.cjj.re.compiler.ex

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass


fun KSClassDeclaration.containsAnnotation(clazz: KClass<*>): Boolean {
    return containsAnnotation(clazz.qualifiedName)
}

fun KSClassDeclaration.containsAnnotation(qualifiedName: String?): Boolean {
    if (qualifiedName == null) {
        return false
    }
    return this.annotations.any { it.annotationType.resolve().declaration.qualifiedName?.asString() == qualifiedName }
}

fun KSClassDeclaration.findKSAnnotation(clazz: KClass<*>): KSAnnotation? {
    return findKSAnnotation(clazz.qualifiedName)
}

fun KSClassDeclaration.findKSAnnotation(qualifiedName: String?): KSAnnotation? {
    if (qualifiedName == null) {
        return null
    }
    return this.annotations.find { it.annotationType.resolve().declaration.qualifiedName?.asString() == qualifiedName }
}




