package com.cjj.re.observer


import kotlin.reflect.KClass

/**
 * <p>
 * SQl拦截器管理类
 * </p>
 *
 * @author CJJ
 * @since 2024-12-14 17:21
 */
object ReObserverManager {


    private val observerMap: HashMap<KClass<*>, MutableList<ReObserver<*>>> = hashMapOf()

    fun <T : Any> addObserver(entityClass: KClass<T>, observer: ReObserver<T>) {
        val list = observerMap.getOrPut(entityClass) { mutableListOf() }
        list.add(observer)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getObserver(entityClass: KClass<T>): MutableList<ReObserver<Any>>? {
        return observerMap[entityClass] as? MutableList<ReObserver<Any>>
    }

    fun <T : Any> removeObserver(entityClass: KClass<T>, observer: ReObserver<T>) {
        observerMap[entityClass]?.remove(observer)
    }

    fun <T : Any> removeObserver(entityClass: KClass<T>) {
        observerMap[entityClass]?.clear()
    }
}