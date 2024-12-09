package com.cjj.roomextension.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author CJJ
 * @since 2024-12-08 16:15
 */
@Entity
data class Student(@PrimaryKey val id:Long, val name:String, val age:Int, val classId:Long)
