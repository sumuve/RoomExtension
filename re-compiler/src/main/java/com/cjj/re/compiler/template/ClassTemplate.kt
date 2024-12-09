package com.cjj.re.compiler.template

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

abstract class ClassTemplate(
    protected val env: SymbolProcessorEnvironment,
    protected val kFile: KSFile
) {

    /**
     * 类名
     */
    protected abstract val className: String

    /**
     * 报名
     */
    protected abstract val packageName: String

    /**
     * 文件扩展名
     */
    protected open val extensionName: String = "kt"

    /**
     * 返回import列表
     */
    protected abstract fun getImportList(): List<String>

    /**
     * 返回类主体
     */
    protected abstract fun getClassTemplate(classSb: StringBuilder)

    open fun createClass() {
        //创建文件流
        val os = env.codeGenerator.createNewFile(//创建新的文件(默认.kt)
            Dependencies(
                true, kFile
            ), packageName, className, extensionName
        )
        val classSb = StringBuilder()
        //写入包名
        if (extensionName == "kt") {
            classSb.append("package $packageName\n\n")
        } else {
            classSb.append("package $packageName;\n\n")
        }
        //写入import列表
        getImportList().forEach {
            if (extensionName == "kt") {
                classSb.append("import $it\n")
            } else {
                classSb.append("import $it;\n")

            }
        }
        classSb.append("\n")
        //写入类主体
        val classBodySb = StringBuilder()
        getClassTemplate(classBodySb)
        classSb.append(classBodySb)
        classSb.append("\n")

        //写入到文件
        os.write(classSb.toString().toByteArray())
        os.flush()
        os.close()
    }


}