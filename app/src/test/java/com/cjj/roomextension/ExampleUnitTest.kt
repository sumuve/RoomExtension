package com.cjj.roomextension

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        test(listOf(1, 2, 3), listOf(1, 2, 3))
    }

    fun test(vararg value: Any) {
        val str = value.joinToString(",") {
            if (it is Collection<*>) {
                it.joinToString(",") { it.toString() }
            } else {
                it.toString()
            }
        }
        println(str)
    }
}