package com.cjj.re.observer

/**
 * <p>
 *
 * </p>
 *
 * @author CJJ
 * @since 2025-05-24 21:20
 */
interface ReObserver<T> {
    fun insert(entity: Collection<T>) {}

    fun update(entity: Collection<T>) {}

    fun delete(entity: Collection<T>) {}

    fun refresh(entity: T) {}
}