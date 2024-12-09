package com.cjj.ksp

import com.cjj.re.compiler.ReSymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @OptIn(ExperimentalCompilerApi::class)
    @Test
    fun addition_isCorrect() {
        val file =
            File("../app/src/main/java/com/cjj/roomextension/bean")
        val sources1 = file.walk().filter { it.isFile }.map { SourceFile.fromPath(it) }.toList()

        val result = KotlinCompilation().apply {
            sources = sources1

            symbolProcessorProviders = listOf(ReSymbolProcessorProvider())

            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()
    }

}
