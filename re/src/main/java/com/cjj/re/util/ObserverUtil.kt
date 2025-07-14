package com.cjj.re.util

import com.cjj.re.observer.ReObserver
import com.cjj.re.observer.ReObserverManager
import kotlin.reflect.KClass

/**
 * <p>
 * 观察者工具类
 * </p>
 *
 * @author CJJ
 * @since 2025-06-08 12:54
 */
object ObserverUtil {

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> observer(
        entityClass: KClass<T>,
        block: (ReObserver<Any>) -> Unit
    ) {
        val entityObservers = ReObserverManager.getObserver(entityClass)
        val anyObservers = ReObserverManager.getObserver(Any::class)
        ReUtil.postMainThread {
            notice(entityObservers, block)
            notice(anyObservers, block)
        }
    }

    private fun notice(observers: MutableList<ReObserver<Any>>?, block: (ReObserver<Any>) -> Unit) {
        if (observers.isNullOrEmpty()) return
        if (observers.size == 1) {
            val observer = observers.first()
            block.invoke(observer)
        } else {
            observers.forEach { block.invoke(it) }
        }
    }

    fun <T : Any> noticeInsert(entityClass: KClass<T>, list: Collection<T>) {
        observer(entityClass) { it.insert(list) }
    }

    fun <T : Any> noticeUpdate(entityClass: KClass<T>, list: Collection<T>) {
        observer(entityClass) { it.update(list) }
    }

    fun <T : Any> noticeDelete(entityClass: KClass<T>, list: Collection<T>) {
        observer(entityClass) { it.delete(list) }
    }

    fun <T : Any> noticeRefresh(entityClass: KClass<T>, entity: T) {
        observer(entityClass) { it.refresh(entity) }
    }

}