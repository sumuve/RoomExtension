package com.cjj.re.compiler.bean

/**
 * <p>
 *
 * </p>
 *
 * @author CJJ
 * @since 2024-06-25 09:04
 */
data class RelevancyPropertyBean(
    val name: String,
    val qualifiedName: String,
    val type: String,
    val isNotNull: Boolean,
    val isList: Boolean,
    val isArray: Boolean,
    val joinFieldList: List<FieldBean>
) {
    val sourceType: String by lazy {
        if (joinFieldList.size == 1) joinFieldList[0].sourceType else "String"
    }
    val targetType: String by lazy {
        if (joinFieldList.size == 1) joinFieldList[0].targetType else "String"
    }
    val targetKey by lazy {
        joinFieldList.joinToString("") { it.targetPropertyName }.replace("?.", "")
    }

    val sourceKey by lazy {
        joinFieldList.joinToString("") { it.sourcePropertyName }.replace("?.", "")
    }
}

data class FieldBean(
    val targetTableName: String,
    val targetFieldName: String,
    val targetPropertyName: String,
    val targetIsNotNull: Boolean,
    val targetType: String,
    val targetIsEmbedded: Boolean,
    val sourceTableName: String,
    val sourceFieldName: String,
    val sourcePropertyName: String,
    val sourceIsNotNull: Boolean,
    val sourceType: String,
    val sourceIsEmbedded: Boolean,
)
