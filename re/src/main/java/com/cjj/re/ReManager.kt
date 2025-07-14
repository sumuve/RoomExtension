package com.cjj.re

import android.os.Handler
import android.os.Looper
import androidx.room.RoomDatabase
import com.cjj.re.interceptor.ReInterceptor
import com.cjj.re.interceptor.ReInterceptorManager
import com.cjj.re.observer.ReObserver
import com.cjj.re.observer.ReObserverManager
import kotlin.reflect.KClass

object ReManager {

    private lateinit var _database: RoomDatabase

    internal val mainHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * 是否打印日志
     */
    var isLog: Boolean = false
        private set

    /**
     * 打印Sql日志时是否格式化
     */
    var isSqlFormat: Boolean = false
        private set

    val database: RoomDatabase
        get() = _database

    fun init(db: RoomDatabase, isLog: Boolean = false, isSqlFormat: Boolean = false) {
        _database = db
        this.isLog = isLog
        this.isSqlFormat = isSqlFormat
    }


    /**
     * 添加拦截器
     */
    fun <T : Any> addInterceptor(entityClass: KClass<T>, interceptor: ReInterceptor<T>) {
        ReInterceptorManager.addInterceptor(entityClass, interceptor)
    }

    /**
     * 添加拦截器
     */
    inline fun <reified T : Any> addInterceptor(interceptor: ReInterceptor<T>) {
        addInterceptor(T::class, interceptor)
    }

    /**
     * 删除拦截器
     * @param interceptor
     */
    inline fun <reified T : Any> removeInterceptor(interceptor: ReInterceptor<T>) {
        ReInterceptorManager.removeInterceptor(T::class, interceptor)
    }

    /**
     * 删除对应类型的所有拦截器
     */
    fun <T : Any> removeInterceptor(entityClass: KClass<T>) {
        ReInterceptorManager.removeInterceptor(entityClass)
    }

    /**
     * 删除对应类型的所有拦截器
     */
    inline fun <reified T : Any> removeInterceptor() {
        removeInterceptor(T::class)
    }

    /**
     * 添加观察者
     * @param entityClass 类型
     * @param observer 观察者
     */
    fun <T : Any> addObserver(entityClass: KClass<T>, observer: ReObserver<T>) {
        ReObserverManager.addObserver(entityClass, observer)
    }

    /**
     * 添加观察者
     * @param observer 观察者
     */
    inline fun <reified T : Any> addObserver(observer: ReObserver<T>) {
        addObserver(T::class, observer)
    }

    /**
     * 删除观察者
     * @param observer 观察者
     */
    inline fun <reified T : Any> removeObserver(observer: ReObserver<T>) {
        ReObserverManager.removeObserver(T::class, observer)
    }

    /**
     * 删除对应类型的所有观察者
     */
    fun <T : Any> removeObserver(entityClass: KClass<T>) {
        ReObserverManager.removeObserver(entityClass)
    }

    /**
     * 删除对应类型的所有观察者
     */
    inline fun <reified T : Any> removeObserver() {
        removeObserver(T::class)
    }
}