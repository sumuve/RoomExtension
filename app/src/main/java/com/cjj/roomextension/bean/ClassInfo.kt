package com.cjj.roomextension.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author CJJ
 * @since 2024-12-08 17:00
 */
@Entity
data class ClassInfo(@PrimaryKey val id:Long,val name:String,val grade:Int)
