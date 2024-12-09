package com.cjj.re.compiler.bean

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Ignore
import com.cjj.re.annotation.ReJoin
import com.cjj.re.compiler.ex.containsAnnotation
import com.cjj.re.compiler.ex.findKSAnnotation
import com.cjj.re.compiler.ex.findKSArgument
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Nullability
import kotlin.reflect.KClass

class PropertiesBean(
    val parent: ClassBean,
    val property: KSPropertyDeclaration,
    val isParentEmbedded: Boolean,
    private val prefix: String = ""
) {

    val propertyName: String = property.simpleName.asString()

    val columnAnnotation by lazy {
        property.findKSAnnotation(ColumnInfo::class.simpleName)
    }

    /**
     * 查询字段名称
     */
    val propertyValue by lazy {
        val columnName = columnAnnotation?.findKSArgument(ColumnInfo::name)?.value?.toString()
            ?: ColumnInfo.INHERIT_FIELD_NAME
        prefix + if (ColumnInfo.INHERIT_FIELD_NAME == columnName) propertyName else columnName
    }


    val type: String by lazy {
        if (isList || isArray) {
            listType
        } else {
            property.type.toString()
        }
    }

    val isList by lazy {
        property.type.toString() == "List" || property.type.toString() == "ArrayList"
    }

    val isArray by lazy {
        property.type.toString() == "Array"
    }

    val listType by lazy {
        property.type.resolve().arguments[0].type.toString()
    }

    val isMutable by lazy {
        property.isMutable
    }

    val isNotNull by lazy {
        property.type.resolve().nullability == Nullability.NOT_NULL
    }

    /**
     * 是否是room忽略字段
     */
    fun isIgnore(): Boolean {
        return property.containsAnnotation(Ignore::class)
    }

    fun isJoin(): Boolean {
        return property.containsAnnotation(ReJoin::class)
    }

    fun containsAnnotation(clazz: KClass<*>): Boolean {
        return property.containsAnnotation(clazz)
    }

    fun findKSAnnotation(clazz: KClass<*>): KSAnnotation? {
        return property.findKSAnnotation(clazz.simpleName)
    }

    val embeddedAnnotation by lazy {
        property.findKSAnnotation(Embedded::class.simpleName)
    }

    val isEmbeddable by lazy { containsAnnotation(Embedded::class) }

    val embeddedPrefix by lazy {
        embeddedAnnotation?.findKSArgument(Embedded::prefix)?.value?.toString() ?: ""
    }

    val ksClass: ClassBean by lazy {
        val declaration = if (isList || isArray) {
            property.type.resolve().arguments[0].type?.resolve()?.declaration as KSClassDeclaration
        } else {
            property.type.resolve().declaration
        }
        ClassBean(declaration as KSClassDeclaration,declaration.containingFile!!, this, isEmbeddable, prefix + embeddedPrefix)
    }
}
