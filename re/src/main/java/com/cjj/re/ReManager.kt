package com.cjj.re

import androidx.room.RoomDatabase

object ReManager {
    private lateinit var _database: RoomDatabase

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
}