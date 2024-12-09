package com.cjj.roomextension.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cjj.roomextension.bean.*

@Database(
    entities = [Student::class, ClassInfo::class],
    version = 1,
    exportSchema = false

)
abstract class RoomExtensionDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: RoomExtensionDatabase? = null

        fun getDatabase(context: Context): RoomExtensionDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomExtensionDatabase::class.java,
                    "word_database"
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}