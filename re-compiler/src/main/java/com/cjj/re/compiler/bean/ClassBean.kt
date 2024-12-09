package com.cjj.re.compiler.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cjj.re.compiler.ex.findKSAnnotation
import com.cjj.re.compiler.ex.findKSArgument
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

class ClassBean(
    val kSClassDeclaration: KSClassDeclaration,
    val kFile: KSFile,
    val parent: PropertiesBean? = null,
    val isEmbedded: Boolean = false,
    val prefix: String = ""
) {
    val className: String
        get() = kSClassDeclaration.simpleName.asString()

    val qualifiedName = kSClassDeclaration.qualifiedName?.asString()

    val tableName by lazy {
        val name = entityInfo?.findKSArgument(Entity::tableName)?.value?.toString()
        if (name != null && name != "") {
            name
        }else{
            className
        }
    }

    val entityInfo by lazy {
        kSClassDeclaration.findKSAnnotation(Entity::class)
    }

    val propertyList by lazy {
        kSClassDeclaration.getDeclaredProperties().map {
            PropertiesBean(this, it, isEmbedded, prefix)
        }
    }

    /**
     * 主键列表
     */
    val primaryKeys by lazy { _getPrimaryKeys() }

    /**
     * 获取主键列表
     */
    private fun _getPrimaryKeys(): List<PropertiesBean> {
        val list = arrayListOf<String>()
        if (entityInfo == null) {
            return emptyList()
        }
        val keys = entityInfo?.findKSArgument(Entity::primaryKeys)
        if (keys != null && keys.value is List<*> && (keys.value as List<*>).isNotEmpty()) {
            @Suppress("UNCHECKED_CAST") list.addAll(keys.value as List<String>)
        } else {
            val primaryKeyProperty =
                propertyList.find { it.containsAnnotation(PrimaryKey::class) }
            if (primaryKeyProperty != null) {
                list.add(primaryKeyProperty.propertyValue)
            }
        }
        val map = propertyList.associateBy { it.propertyValue }
        return list.mapNotNull {
            map[it]
        }
    }


}