package com.cjj.re.util

import com.cjj.re.interceptor.ReInterceptor
import com.cjj.re.interceptor.ReInterceptorManager
import kotlin.reflect.KClass

/**
 * <p>
 * 拦截器工具类
 * </p>
 *
 * @author CJJ
 * @since 2025-06-08 12:53
 */
object InterceptorUtil {

    @Suppress("UNCHECKED_CAST")
    private inline fun <T : Any> interceptor(
        entityClass: KClass<T>,
        entityList: Collection<T>,
        crossinline block: (ReInterceptor<Any>, item: Any) -> Any?
    ): Collection<T> {
        if (entityList.isEmpty()) {
            return entityList
        }
        val entityInterceptors = ReInterceptorManager.getInterceptor(entityClass) as? MutableList<ReInterceptor<Any>>
        val anyInterceptor = ReInterceptorManager.getInterceptor(Any::class)
        if (entityInterceptors.isNullOrEmpty() && anyInterceptor.isNullOrEmpty()) {
            return entityList
        }
        var sequence: Sequence<Any> = entityList.asSequence()
        sequence = mapNotNull(sequence, entityInterceptors, block)
        sequence = mapNotNull(sequence, anyInterceptor, block)
        val newList = sequence.toList()
        return newList as List<T>
    }

    @Suppress("UNCHECKED_CAST")
    inline fun mapNotNull(
        sequence: Sequence<Any>,
        interceptors: MutableList<ReInterceptor<Any>>?,
        crossinline block: (ReInterceptor<Any>, Any) -> Any?
    ): Sequence<Any> {
        if (interceptors.isNullOrEmpty()) {
            return sequence
        }
        return if (interceptors.size == 1) {
            val reInterceptor = interceptors.first()
            sequence.mapNotNull { item -> block(reInterceptor, item) }
        } else {
            sequence.mapNotNull { item ->
                interceptors.fold(item) { item, it -> block(it, item) ?: return@mapNotNull null }
            }
        }
    }


    fun <T : Any> interceptorInsert(
        entityClass: KClass<T>,
        list: Collection<T>
    ) = interceptor(entityClass, list) { interceptors, item ->
        interceptors.insert(item)
    }


    fun <T : Any> interceptorUpdate(
        entityClass: KClass<T>,
        list: Collection<T>
    ) = interceptor(entityClass, list) { interceptors, item ->
        interceptors.update(item)
    }

    fun <T : Any> interceptorDelete(
        entityClass: KClass<T>,
        list: Collection<T>
    ) = interceptor(entityClass, list) { interceptors, item ->
        interceptors.delete(item)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> interceptorRefresh(
        entityClass: KClass<T>,
        entity: Any
    ): T? {
        val interceptors = ReInterceptorManager.getInterceptor(entityClass) as? MutableList<ReInterceptor<Any>>
        val anyInterceptors = ReInterceptorManager.getInterceptor(Any::class)
        if (interceptors.isNullOrEmpty() && anyInterceptors.isNullOrEmpty()) {
            return entity as? T
        }
        fun fold(entity: Any?, interceptor: MutableList<ReInterceptor<Any>>?): Any? {
            var newEntity: Any? = entity
            if (interceptor.isNullOrEmpty() || newEntity == null) {
                return entity
            }
            for (interceptor in interceptor) {
                newEntity = newEntity?.let { interceptor.refresh(it) }
                newEntity ?: break
            }
            return newEntity
        }

        return fold(fold(entity, interceptors), anyInterceptors) as? T

    }
}