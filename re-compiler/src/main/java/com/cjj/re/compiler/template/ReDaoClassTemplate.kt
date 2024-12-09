package com.cjj.re.compiler.template

import androidx.room.Entity
import androidx.room.Ignore
import com.cjj.re.annotation.JoinProperty
import com.cjj.re.annotation.ReJoin
import com.cjj.re.compiler.bean.ClassBean
import com.cjj.re.compiler.bean.FieldBean
import com.cjj.re.compiler.bean.PropertiesBean
import com.cjj.re.compiler.bean.RelevancyPropertyBean
import com.cjj.re.compiler.env.Env
import com.cjj.re.compiler.ex.containsAnnotation
import com.cjj.re.compiler.ex.findKSArgument
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFile

class ReDaoClassTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile,
    private val classBean: ClassBean
) :
    ClassTemplate(env, kFile) {
    private val sourceClassName: String = classBean.className
    override val className: String = "__Re${classBean.className}Dao"
    override val packageName: String = "com.cjj.re.dao"

    /**
     * 关联查询信息
     */
    private val relevancyPropertyList = getRelevancyList()

    override fun getImportList(): List<String> {
        val arrayListOf = arrayListOf(
            "androidx.sqlite.db.SimpleSQLiteQuery",
            classBean.qualifiedName!!,
            "com.cjj.re.aggregate.*",
//            "com.cjj.re.aggregate.table.Re${sourceClassName}Aggregate",
            "com.cjj.re.base.ReBaseDao",
            "com.cjj.re.base.ReBaseCoreDao",
            "com.cjj.re.dao.core.__Re${sourceClassName}CoreDao",
            "com.cjj.re.dao.core.__Re${sourceClassName}CoreDao_Impl",
//            "com.cjj.re.util.DaoUtil",
            "com.cjj.re.wrapper.QueryWrapper",
            "com.cjj.re.wrapper.AggregateWrapper",
            "com.cjj.re.keys.AggregateFunctions",
            "com.cjj.re.ReManager",
            "com.cjj.re.util.ReUtil",
            "kotlin.reflect.KProperty",
        )
        if (relevancyPropertyList.isNotEmpty()) {
            val set = relevancyPropertyList.asSequence().map { it.qualifiedName }
                .toSet()
            arrayListOf.addAll(set)
            arrayListOf.add("com.cjj.re.dao.*")
        }
        return arrayListOf
    }

    override fun getClassTemplate(sb: StringBuilder) {
        sb.append("object ${className} : ReBaseDao<${sourceClassName}>() {\n\n")
        sb.append("    override val dao = __Re${sourceClassName}CoreDao_Impl(ReManager.database)\n\n")
//        sb.append("        get() = DaoUtil.Instance.dao\n\n")

        sb.append("    override val aggregate = ReAggregate(dao, ${sourceClassName}::class)\n\n")

        val relevancyGroup = relevancyPropertyList.groupBy { it.type }
        sb.append("    override fun query(relevance: Boolean, wrapper: QueryWrapper.() -> Unit): List<${sourceClassName}> {\n")
        sb.append("        val query = QueryWrapper(${sourceClassName}::class)\n")
        sb.append("        wrapper.invoke(query)\n")
        sb.append("        val sql = ReUtil.getSql(query)\n")
        sb.append("        val list = dao.getList(sql)\n")
        if (relevancyGroup.isNotEmpty()) {//添加关联查询字段
            sb.append("        if (relevance && list.isNotEmpty()) {\n")
            sb.append(
                "            ${
                    relevancyGroup.map { (k, _) -> "query$k(list)\n" }.joinToString("            ")
                }"
            )
            sb.append("        }\n")
        }

        sb.append("        return list\n")
        sb.append("    }\n\n")

        if (classBean.primaryKeys.isNotEmpty()) {
            sb.append("    fun queryByPrimaryKey(${classBean.primaryKeys.joinToString(", ") { field -> "${field.propertyValue}: ${field.type}" }}): ${classBean.className}? {\n")
            sb.append("        return dao.getByPrimaryKey(${classBean.primaryKeys.joinToString(", ") { field -> field.propertyValue }})\n")
            sb.append("    }\n\n")
        }

        sb.append("    override fun refresh(entity: ${sourceClassName}): Boolean {\n")
        if (classBean.primaryKeys.isEmpty()) {
            sb.append("        return false\n")
        } else {
            val primaryKeyMap = classBean.primaryKeys.associateBy { it.propertyName }
            val fieldList =
                classBean.propertyList.filter {
                    it.isMutable && !primaryKeyMap.containsKey(it.propertyName) && !it.containsAnnotation(
                        Ignore::class
                    )
                }
                    .toList()
            if (fieldList.isEmpty()) {
                sb.append("        return true\n")
            } else {
                sb.append("        val newEntity = queryByPrimaryKey(${classBean.primaryKeys.joinToString(", ") { "entity." + it.propertyName }})\n")
                sb.append("        if (newEntity == null) {\n")
                sb.append("            return false\n")
                sb.append("        } else {\n")
                fieldList.forEach {
                    sb.append("            entity.${it.propertyName} = newEntity.${it.propertyName}\n")
                }
                sb.append("            return true\n")
                sb.append("        }\n")
            }
        }
        sb.append("    }\n\n")
        if (relevancyGroup.isNotEmpty()) {
            relevancyGroup.forEach { (targetTableClassName, relevancyList) ->
                val fieldAllList = relevancyList.flatMap { it.joinFieldList }
                sb.append("    private fun query${targetTableClassName}(list: List<${sourceClassName}>) {\n")
                val queryKeyMap = hashMapOf<String, HashSet<FieldBean>>()
                //添加用于查询的list
                fieldAllList.forEach {
                    if (!queryKeyMap.containsKey(it.targetFieldName)) {
                        sb.append("        val keysBy${it.targetFieldName} = arrayListOf<${it.sourceType}>()\n")
                    }
                    queryKeyMap.getOrPut(it.targetFieldName) { hashSetOf() }.add(it)
                }
                sb.append("\n")
                sb.append("        list.forEach {\n")
                //查询list添加值
                queryKeyMap.forEach { (targetFieldName, fieldList) ->
                    fieldList.forEach { field ->
                        if (field.sourceIsNotNull) {
                            sb.append("            keysBy${targetFieldName}.add(it.${field.sourcePropertyName})\n")
                        } else {
                            sb.append("            if(it.${field.sourcePropertyName} != null) {\n")
                            sb.append("                keysBy${targetFieldName}.add(it.${field.sourcePropertyName}!!)\n")
                            sb.append("            }\n")
                        }
                    }
                }
                sb.append("        }\n\n")

                //如果key值都为空，则不再查询
                sb.append("        if (${queryKeyMap.keys.joinToString(" && ") { "keysBy${it}.isEmpty()" }}) {\n")
                sb.append("           return\n")
                sb.append("        }\n\n")

                //创建查询结果
                sb.append("        val queryList = Re${targetTableClassName}Dao.query {\n")
                sb.append("            where {\n")

                val keySet = hashSetOf<String>()

                if (relevancyList.size == 1) {//不需要查询多种条件，不需要使用or
                    fieldAllList.forEach { field ->
                        if (field.targetIsEmbedded) {
                            sb.append("                `in`(\"${field.targetFieldName}\", keysBy${field.targetFieldName}, tableName = \"${field.targetTableName}\")\n")
                        } else {
                            sb.append("                `in`(${targetTableClassName}::${field.targetPropertyName}, keysBy${field.targetFieldName})\n")
                        }

                    }
                } else {//使用or创建多个查询条件
                    relevancyList.forEach { property ->
                        if (!keySet.contains(property.targetKey)) {//去除重复查询条件
                            keySet.add(property.targetKey)
                            sb.append("                if (${property.joinFieldList.joinToString(" && ") { field -> "keysBy${field.targetFieldName}.isNotEmpty()" }}) {\n")
                            sb.append("                    or {\n")
                            property.joinFieldList.forEach { field ->
                                if (field.targetIsEmbedded) {
                                    sb.append("                        `in`(\"${field.targetFieldName}\", keysBy${field.targetFieldName}, tableName = \"${field.targetTableName}\")\n")
                                } else {
                                    sb.append("                        `in`(${targetTableClassName}::${field.targetPropertyName}, keysBy${field.targetFieldName})\n")
                                }
                            }
                            sb.append("                    }\n")
                            sb.append("                }\n")
                        }
                    }
                }
                sb.append("            }\n")
                sb.append("        }\n")
                sb.append("\n")
                //查询结果为空，停止方法
                sb.append("        if (queryList.isEmpty()) {\n")
                sb.append("            return\n")
                sb.append("        }\n")
                sb.append("\n")


                //添加用于保存查询结果的map
                keySet.clear()
                relevancyList.forEach { property ->
                    if (!keySet.contains(property.targetKey)) {//去重
                        keySet.add(property.targetKey)
                        sb.append("        val ${property.targetKey}Map = hashMapOf<${property.sourceType}, ${if (property.isList || property.isArray) "ArrayList<${targetTableClassName}>" else targetTableClassName}>()\n")
                    }
                }

                sb.append("        \n")
                //遍历查询结果
                sb.append("        queryList.forEach {\n")
                keySet.clear()
                val propertyMap = hashMapOf<String, ArrayList<RelevancyPropertyBean>>()
                relevancyList.forEach { property ->
                    //通过sourceKey 对需要赋值的字段 进行分组
                    propertyMap.getOrPut(property.sourceKey) { arrayListOf() }.add(property)
                    if (!keySet.contains(property.targetKey)) {//去重
                        keySet.add(property.targetKey)
                        val nullFieldBeanList = property.joinFieldList.filter { !it.targetIsNotNull }
                        //map key Long与Int不需要toString
                        val keyStr = when (property.sourceType) { //
                            "Long", "Int" -> "it.${property.joinFieldList[0].targetPropertyName}${if (property.joinFieldList[0].targetIsNotNull) "" else "!!"}"
                            else -> property.joinFieldList.joinToString(" + \"-@@-\" + ") { "it.${it.targetPropertyName}${if (it.targetIsNotNull) "" else "!!"}.toString()" }
                        }
                        var blank = ""
                        if (nullFieldBeanList.isNotEmpty()) {
                            blank = "    "
                            sb.append("            if (${nullFieldBeanList.joinToString(" && ") { "it.${it.targetPropertyName} != null" }}) {\n")
                        }
                        //判断查询结果是否需要保存到List中
                        if (property.isList || property.isArray) {
                            sb.append("$blank            ${property.targetKey}Map.getOrPut(${keyStr}){arrayListOf()}.apply{ add(it) }\n")
                        } else {
                            sb.append("$blank            ${property.targetKey}Map.put($keyStr, it)\n")
                        }
                        if (nullFieldBeanList.isNotEmpty()) {
                            sb.append("            }\n")
                        }
                    }

                }
                sb.append("        }\n\n")
                //遍历源list，进行设值
                sb.append("        list.forEach {\n")

                keySet.clear()
                relevancyList.forEach { property ->
                    if (!keySet.contains(property.sourceKey)) {//去重
                        keySet.add(property.sourceKey)
                        //创建map key
                        val nullFieldBeanList = property.joinFieldList.filter { !it.sourceIsNotNull }
                        var blank = ""
                        if (nullFieldBeanList.isNotEmpty()) {
                            blank = "    "
                            sb.append("            if (${nullFieldBeanList.joinToString(" && ") { "it.${it.sourcePropertyName} != null" }}) {\n")
                        }
                        val keyStr = when (property.sourceType) {
                            "Long", "Int" -> "it.${property.joinFieldList[0].sourcePropertyName}${if (property.joinFieldList[0].sourceIsNotNull) "" else "!!"}"
                            else -> property.joinFieldList.joinToString(" + \"-@@-\" + ") { "it.${it.sourcePropertyName}${if (it.sourceIsNotNull) "" else "!!"}.toString()" }
                        }
                        //查询当前item是否有值
                        sb.append("$blank            val ${property.sourceKey}key = $keyStr\n")
                        sb.append("$blank            ${property.targetKey}Map[${property.sourceKey}key]?.let { __value ->\n")
//                        sb.append("                val temp = ${property.targetKey}Map[${property.sourceKey}key]!!\n")

                        //通过sourceKey查询相同条件的赋值字段
                        val list = propertyMap[property.sourceKey]
                        if (!list.isNullOrEmpty()) {

                            if (property.isList || property.isArray) {//查询结果是否未数组
                                sb.append("$blank                if (__value.isNotEmpty()) {\n")
                                list.forEach {
                                    if (it.isList) {
                                        sb.append("$blank                    it.${it.name} = __value\n")
                                    } else if (it.isArray) {
                                        sb.append("$blank                    it.${it.name} = __value.toTypedArray()\n")
                                    } else {
                                        sb.append("$blank                    it.${it.name} = __value[0]\n")
                                    }
                                }
                                sb.append("$blank                }\n")
                            } else {
                                list.forEach {
                                    sb.append("$blank                it.${it.name} = __value\n")
                                }
                            }
                        }
                        sb.append("$blank            }\n")
                        if (nullFieldBeanList.isNotEmpty()) {
                            sb.append("            }\n")
                        }

                    }

                    sb.append("\n")
                }
                sb.append("        }\n")
                sb.append("    }\n\n")
            }
        }
        sb.append("}\n\n")
    }

    private fun getRelevancyList(): List<RelevancyPropertyBean> {
        return classBean.propertyList
            .filter { it.isJoin() }
            .filter {
                val ksClass = it.ksClass
                val isEntity = ksClass.kSClassDeclaration.containsAnnotation(Entity::class)
                if (!isEntity) {
                    Env.environment.logger.error("${ksClass.className}不是@Entity的注解类", it.property)
                }
                isEntity
            }.filter {
                if (!it.isMutable) {
                    Env.environment.logger.error("${className}.${it.propertyName}为val,不可修改", it.property)

                }
                it.isMutable
            }.map {
                val annotation = it.findKSAnnotation(ReJoin::class)!!
                val list = annotation.findKSArgument(ReJoin::joinProperties)?.value

                val sourcePropertyMap = hashMapOf<String, PropertiesBean>()
                val targetPropertyMap = hashMapOf<String, PropertiesBean>()
                savePropertyData(classBean, sourcePropertyMap)
                savePropertyData(it.ksClass, targetPropertyMap)

                val joinList = arrayListOf<Pair<String, String>>()
                if (list == null || list !is List<*> || list.isEmpty()) {
                    val joinProperty =
                        annotation.findKSArgument(ReJoin::joinProperty)?.value?.toString()
                    if (joinProperty == null) {
                        Env.environment.logger.error(
                            "${className}.${it.propertyName}joinProperty与joinProperties都为空值",
                            it.property
                        )
                    } else {
                        if (it.ksClass.primaryKeys.size != 1) {
                            Env.environment.logger.error(
                                "${it.ksClass.className}primaryKey不唯一,${it.propertyName}无法使用joinProperty",
                                it.property
                            )
                        } else {
                            val sourceColumnName = joinProperty
                            val targetColumnName = it.ksClass.primaryKeys[0].propertyName
                            joinList.add(targetColumnName to sourceColumnName)

                        }
                    }
                } else {
                    @Suppress("UNCHECKED_CAST") val joinPropertys = list as ArrayList<KSAnnotation>
                    joinPropertys.forEach { a ->
                        a.arguments.associateBy({ k -> k.name!!.asString() }) { k ->
                            k.value.toString()
                        }.let { map ->
                            val sourceColumnName = map[JoinProperty::parentColumn.name]!!
                            val targetColumnName = map[JoinProperty::entityColumn.name]!!
                            joinList.add(targetColumnName to sourceColumnName)
                        }
                    }
                }
                if (joinList.isEmpty()) {
                    null
                } else {
                    val fieldList = joinList.asSequence().map { pair ->
                        val targetColumnName = pair.first
                        val sourceColumnName = pair.second
                        val sourceProperty = sourcePropertyMap[sourceColumnName]
                        val targetProperty = targetPropertyMap[targetColumnName]
                        if (targetProperty == null) {
                            Env.environment.logger.error(
                                "${it.ksClass.className} 中没有 $targetColumnName 字段, 请确认后再试",
                                it.property
                            )
                        }
                        if (sourceProperty == null) {
                            Env.environment.logger.error(
                                "${classBean.className} 中没有 $sourceColumnName 字段, 请确认后再试",
                                it.property
                            )
                        }
                        if (targetProperty == null || sourceProperty == null) {
                            null
                        } else if (targetProperty.type != sourceProperty.type) {
                            Env.environment.logger.error(
                                "$className.${sourceProperty.propertyName} 与 ${it.ksClass.className}.${targetProperty.propertyName} 类型不一致! ${targetProperty.type} != ${sourceProperty.type}",
                                it.property
                            )
                            null
                        } else {
                            val fieldBean = FieldBean(
                                targetProperty.parent.tableName,
                                targetColumnName,
                                getPropertyName(targetProperty),
                                targetProperty.isNotNull,
                                targetProperty.type,
                                targetProperty.isEmbeddable || targetProperty.isParentEmbedded,
                                classBean.tableName,
                                sourceColumnName,
                                getPropertyName(sourceProperty),
                                sourceProperty.isNotNull,
                                sourceProperty.type,
                                sourceProperty.isEmbeddable || sourceProperty.isParentEmbedded,
                            )
//                            Env.environment.logger.warn(fieldBean.toString())
                            fieldBean
                        }
                    }.filterNotNull().toList()
                    RelevancyPropertyBean(
                        getPropertyName(it),
                        it.ksClass.qualifiedName!!,
                        it.type,
                        it.isNotNull,
                        it.isList,
                        it.isArray,
                        fieldList
                    )
                }
            }
            .filterNotNull()
            .filter { it.joinFieldList.isNotEmpty() }.toList()
    }

    private fun savePropertyData(classBean: ClassBean, propertyMap: HashMap<String, PropertiesBean>) {
        classBean.propertyList.forEach {
            propertyMap[it.propertyValue] = it
            if (it.isEmbeddable) {
                savePropertyData(it.ksClass, propertyMap)
            }
        }
    }

    private fun getPropertyName(propertyBean: PropertiesBean): String {
        if (!propertyBean.isEmbeddable && !propertyBean.isParentEmbedded) {
            return propertyBean.propertyName
        } else {
            fun getParentPropertyName(propertyBean: PropertiesBean): String {
                if (!propertyBean.isParentEmbedded) {
                    return ""
                }
                return propertyBean.parent.parent?.let {
                    getParentPropertyName(it) + (if (it.isNotNull) "${it.propertyName}." else "${it.propertyName}?.")
                } ?: ""
            }
            return getParentPropertyName(propertyBean) + propertyBean.propertyName
        }
    }
}
