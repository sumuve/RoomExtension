package com.cjj.re.interceptor

import kotlin.reflect.KClass

/**
 * <p>
 * SQl拦截器管理类
 * </p>
 *
 * @author CJJ
 * @since 2024-12-14 17:21
 */
object ReInterceptorManager {

    private val interceptorMap: HashMap<KClass<*>, MutableList<ReInterceptor<*>>> = hashMapOf()

    fun <T : Any> addInterceptor(entityClass: KClass<T>, interceptor: ReInterceptor<T>) {
        val list = interceptorMap.getOrPut(entityClass) { mutableListOf() }
        list.add(interceptor)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInterceptor(entityClass: KClass<T>): MutableList<ReInterceptor<T>>? {
        return interceptorMap[entityClass] as? MutableList<ReInterceptor<T>>
    }

    fun <T : Any> removeInterceptor(entityClass: KClass<T>, interceptor: ReInterceptor<T>) {
        interceptorMap[entityClass]?.remove(interceptor)
    }

    fun <T : Any> removeInterceptor(entityClass: KClass<T>) {
        interceptorMap[entityClass]?.clear()
    }
}