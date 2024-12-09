package com.cjj.roomextension

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cjj.roomextension.bean.ClassInfo
import com.cjj.roomextension.bean.Student
import com.cjj.roomextension.database.RoomExtensionDatabase
import com.cjj.re.ReDao
import com.cjj.re.ReManager
import com.cjj.re.keys.*
import com.cjj.re.wrapper.QueryWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val TAG: String = "ReTag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化
        val database = RoomExtensionDatabase.getDatabase(this)
        ReManager.init(
            db = database,
            isLog = true,//是否打印日志
            isSqlFormat = true//打印SQL时是否格式化
        )
        //插入数据
        findViewById<View>(R.id.btn_init).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val count = ReDao.count<Student>()
                if (count == 0L) {
                    val classList = listOf(
                        ClassInfo(1, "一年级", 1),
                        ClassInfo(2, "二年级", 2),
                        ClassInfo(3, "三年级", 3),
                    )
                    //插入数据
                    val classRowIds = ReDao.insert(classList)
                    Log.i(TAG, classRowIds.joinToString(",") { it.toString() })


                    val students = listOf(
                        Student(1, "藩新晴", 6, 1),
                        Student(2, "盘英武", 7, 1),
                        Student(3, "阿珉瑶", 6, 1),
                        Student(4, "都暮雨", 7, 2),
                        Student(5, "安雅容", 8, 2),
                        Student(6, "苑丽君", 8, 2),
                        Student(7, "西门雅", 8, 3),
                        Student(8, "封柔蔓", 9, 3),
                        Student(10, "晁宏富", 8, 3),
                    )

                    //插入数据
                    val studentRowIds = ReDao.insert(students)
                    Log.i(TAG, studentRowIds.joinToString(",") { it.toString() })


                    toast("插入${classRowIds.size + studentRowIds.size}条数据")
                } else {
                    toast("数据已经存在!")
                }
            }
        }
        //查询数据
        findViewById<View>(R.id.btn_query).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val queryList: List<Student> = ReDao.query<Student> {
                    where {
                        `in`(Student::classId, 1, 2)
                        between(Student::age, 6, 7)
                    }
                }
                Log.i(TAG, queryList.toString())
                toast("查询到${queryList.size}条数据")
            }
        }
        //子查询
        findViewById<View>(R.id.btn_subquery).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val queryList: List<Student> = ReDao.query<Student> {
                    where {
                        `in`(
                            Student::classId, QueryWrapper(ClassInfo::class)
                                .queryColumn(ClassInfo::id)
                                .where {
                                    `in`(ClassInfo::grade, 1, 2)
                                })
                    }
                }
                Log.i(TAG, queryList.toString())
                toast("查询到${queryList.size}条数据")
            }
        }
        //数量查询
        findViewById<Button>(R.id.btn_query_count)
            .setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    val count: Long = ReDao.count<Student> {
                        where {
                            or {
                                eq(Student::classId, 3)
                            }
                            between(Student::id, 1, 3)

                        }
                    }
                    toast("查询到${count}条数据")
                }
            }
        //多表联查
        findViewById<Button>(R.id.btn_query_join)
            .setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    val queryList: List<Student> = ReDao.query<Student> {
//                        join(ClassInfo::class, ClassInfo::id, Student::classId)
                        join(ClassInfo::class) {
                            eq(ClassInfo::id, Student::classId)
                        }
                        where {
                            eq(ClassInfo::grade, 1)
                        }
                    }
                    Log.i(TAG, queryList.toString())
                    toast("查询到${queryList.size}条数据")
                }
            }
        //聚合查询
        findViewById<Button>(R.id.btn_query_aggregate)
            .setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    val groupByCount: Map<Student, Long> = ReDao.aggregate<Student>()
                        .long()
                        .groupByCount(Student::id) {
                            where {
                                ge(Student::age, 7)
                            }
                            groupBy(Student::classId) {
                                ge(Student::id, 2, AggregateFunctions.COUNT)
                            }
                        }
                    Log.i(TAG, groupByCount.toString())
                    toast("查询到${groupByCount.size}条数据")
                }
            }
        //删除数据
        findViewById<Button>(R.id.btn_delete)
            .setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    val deleteStuCount = ReDao.delete(ReDao.query<Student> {})
                    val deleteClassCount = ReDao.delete(ReDao.query<ClassInfo> {})
                    toast("删除到${deleteStuCount + deleteClassCount}条数据")

                }
            }

    }

    private suspend fun toast(str: String) = withContext(Dispatchers.Main) {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_SHORT).show()
    }

}