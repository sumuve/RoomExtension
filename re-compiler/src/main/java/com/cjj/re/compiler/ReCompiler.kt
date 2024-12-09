package com.cjj.re.compiler

import com.cjj.re.compiler.bean.ClassBean
import com.cjj.re.compiler.env.Env
import com.cjj.re.compiler.template.*
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration

class ReCompiler(
    private val environment: SymbolProcessorEnvironment,
    private val list: MutableList<KSClassDeclaration>
) {

    fun visitClassDeclaration() {
        environment.logger.info(list.size.toString())
        if (list.isEmpty()) {
            return
        }
        val classBeans = list.map {
            val classBean = ClassBean(it,it.containingFile!!)
            Env.putClazz(classBean.className, classBean)
            classBean
        }
//        val kFile = list.first().containingFile!!

        //创建成员名称对应查找类Properties
//        environment.logger.info("创建成员名称对应查找类Properties")
//        PropertiesClassTemplate(environment, kFile, classBeans).createClass()

        //创建基类Dao
//        environment.logger.info("创建基类Dao")
//        BaseDaoClassTemplate(environment, kFile).createClass()
//        environment.logger.info("创建基类Aggregate")
//        BaseAggregateClassTemplate(environment, kFile).createClass()
//        NumberAggregateClassTemplate(environment, kFile).createClass()
//        arrayOf("Int", "Long", "Float", "Double").forEach {
//            AggregateClassTemplate(it, environment, kFile).createClass()
//        }
        //创建实体对应的Dao
        environment.logger.info("创建实体对应的Dao")
        classBeans.forEach {
            environment.logger.info(it.className)
//            ReAggregateClassTemplate(environment, kFile, it).createClass()
            //创建ReCoreDao
            environment.logger.info("创建ReCoreDao")
            ReCoreDaoClassTemplate(environment, it.kFile, it).createClass()
            ReDaoClassTemplate(environment, it.kFile, it).createClass()
            DataBaseClassTemplate(environment, it.kFile, it).createClass()
            ReTableInfoTemplate(environment, it.kFile, it).createClass()
        }
        //创建DaoUtil
//        environment.logger.info("创建实体对应的DaoUtil")
//        DaoUtilClassTemplate(environment, kFile).createClass()


    }
}