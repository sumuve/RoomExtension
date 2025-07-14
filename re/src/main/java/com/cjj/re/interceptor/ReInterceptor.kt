package com.cjj.re.interceptor

/**
 * <p>
 * 插入与更新的SQL拦截器
 * </p>
 *
 * @author CJJ
 * @since 2024-12-14 17:19
 */
interface ReInterceptor<T> {
    fun insert(entity: T): T? {
        return entity
    }

    fun update(entity: T): T? {
        return entity
    }

    fun delete(entity: T): T? {
        return entity
    }

    fun refresh(entity: T): T? {
        return entity
    }
}