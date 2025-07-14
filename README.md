### RoomExtension简介
* RoomExtension基于KSP,自动生成insert、update、query、delete方法,并且可以以DSL方式查询数据库
* 支持多表联查、子查询、聚合函数、字符函数、数值函数

### 集成方式:
#### 添加KSP插件
在项目下的`build.gradle`添加
```groovy
plugins {
    id 'com.google.devtools.ksp' version '2.0.0-1.0.23' apply false
}
```
或者`build.gradle.kts`
```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
}
```
然后,在模块级 `build.gradle` 文件中启用 KSP：
```groovy
plugins {
    id 'com.google.devtools.ksp'
}
```
或者`build.gradle.kts`
```kotlin
plugins {
    id("com.google.devtools.ksp")
}
```
#### 添加RoomExtension
```groovy
dependencies {
    //添加RoomExtension
    implementation "io.github.sumuve:room-extension:1.1.1"
    ksp "io.github.sumuve:room-extension-compiler:1.1.1"
    //添加Room
    implementation "androidx.room:room-runtime:2.6.1"
    ksp "androidx.room:room-compiler:2.6.1"
    implementation "androidx.room:room-ktx:2.6.1"
}
```
或者`build.gradle.kts`
```kotlin
dependencies {
    //添加RoomExtension
    implementation("io.github.sumuve:room-extension:1.1.1")
    ksp("io.github.sumuve:room-extension-compiler:1.1.1")
    //添加Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
}
```
### 使用方式：
#### 初始化
```kotlin
//初始化Room
val db = Room.databaseBuilder(
    applicationContext,
    RoomExtensionDatabase::class.java, "database-name"
).build()
//初始化RoomExtension
ReManager.init(
    db = database,      //数据库
    isLog = true,       //是否打印日志,默认为false
    isSqlFormat = false  //打印SQL时是否格式化,默认为false
)
```
isSqlFormat为false时,sql参数将显示`?`
```
SELECT Student.* FROM Student WHERE (Student.classId =  ? )
[1]
query count:4  time:1ms
```
isSqlFormat为true时`?`将被参数替代
```
SELECT Student.* FROM Student WHERE (Student.classId =  1 )
query count:4  time:1ms
```

---
#### 插入数据
```kotlin
//插入一条或者多条数据
val rows:LongArray= ReDao.insert(Student(),Student())
```
```kotlin
//插入列表
val students: List<Student> = listOf()
val rows: LongArray = ReDao.insert(students)
```
`rows`为插入成功的行列表

---
#### 更新数据
```kotlin
//一条或者多条数据
val count:Int = ReDao.update(student)
```
```kotlin
//更新列表
val students: List<Student> = listOf()
val count:Int = ReDao.insert(students)
```
`count`为更新成功的条目数量

---
#### 删除数据
```kotlin
//一条或者多条数据
val count:Int = ReDao.delete(student)
```
```kotlin
//删除所有数据
val count:Int = ReDao.delete(ReDao.query<Student> {})
```
`count`为更新成功的条目数量

---
### 查询数据
#### 查询所有数据
```kotlin
val queryList: List<Student> = ReDao.query<Student> {}
```
#### 按条件查询
```kotlin
//查询student表中classId为1、2,6<=age<=7的数据列表
val queryList: List<Student> = ReDao.query<Student> {
    where {
        `in`(Student::classId, 1, 2)
        between(Student::age, 6, 7)
    }
}
```
#### 子查询
```kotlin
//查询student表中关联的班级grade为1、2的的数据列表
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
```
>使用子查询时，必须添加queryColumn方法指定查询的列
#### 多表联查
```kotlin
//查询班级grade为1的student列表
val queryList: List<Student> = ReDao.query<Student> {
    join(ClassInfo::class, ClassInfo::id, Student::classId)
    //或者
    //join(ClassInfo::class){
    //    eq(ClassInfo::id, Student::classId)
    //}
    where {
        eq(ClassInfo::grade, 1)
    }
}
```
#### 数量查询
```kotlin
////查询student表中id=1数据数量
val count:Int = ReDao.count<Student> { 
    where {
        eq(Student::id, 1)
    }
}
```
#### 聚合查询
```kotlin
//总数分组查询
//查询每个班age>=7的学生总人数
val groupByCount: Map<Student, Long> = ReDao.aggregate<Student>()
    .long()
    .groupByCount(Student::id) {
        where {//查询条件
            ge(Student::age, 7)
        }
        groupBy(Student::classId) {//分组结果过滤,等效为
            //只查询总人数>=2的结果
            //等效为 GROUP BY Student.classId HAVING (COUNT(Student.id) >= 2)
            //AggregateFunctions.COUNT 总数查询
            //AggregateFunctions.SUM   和查询
            //AggregateFunctions.AVG   平均值查询
            //AggregateFunctions.MIN   最小值查询
            //AggregateFunctions.MAX   最大值查询
            ge(Student::id, 2, AggregateFunctions.COUNT)
        }
    }
```
>groupByCount方法只能在返回的类型为long时使用
```kotlin
//查询每个班age最大的人
val groupByMax: Map<Student, Long> = ReDao.aggregate<Student>()
    .long()//支持int()、long()、float()、double()四种基本数据
    .groupByMax(Student::age) {//支持groupByMax(),groupByMin(),groupByAvg(),groupBySum()四种函数
        groupBy(Student::classId)
    }
```
```kotlin
//max最大值
val max: Long   = ReDao.aggregate<Student>().long().max(Student::age)
//min最小值
val min: Int    = ReDao.aggregate<Student>().int().min(Student::age)
//avg平均数
val avg: Double = ReDao.aggregate<Student>().double().avg(Student::age)
//sum和
val sum: Float  = ReDao.aggregate<Student>().float().sum(Student::age)
```

#### 拦截器

```kotlin
//全局拦截器 可设置多个 类型为Any即可
ReManager.addInterceptor(object : ReInterceptor<Any> {
        override fun insert(entity: Any): Any? {
            //返回空的时候该元素将被拦截,不做处理
            //统一修改更新时间
            return super.insert(entity)
        }
        override fun update(entity: Any): Any? {
            return super.update(entity)
        }
    
        override fun delete(entity: Any): Any? {
            return super.delete(entity)
        }
    
        override fun refresh(entity: Any): Any? {
            return super.refresh(entity)
        }
    
    })

//类型拦截器 可设置多个
ReManager.addInterceptor(object : ReInterceptor<ClassType> {
    override fun insert(entity: ClassType): ClassType? {
        return super.insert(entity)
    }

    override fun update(entity: ClassType): ClassType? {
        return super.update(entity)
    }

    override fun delete(entity: ClassType): ClassType? {
        return super.delete(entity)
    }

    override fun refresh(entity: ClassType): ClassType? {
        return super.refresh(entity)
    }
})
```

#### 观察者

```kotlin
//设置全局观察者
ReManager.addObserver(object : ReObserver<Any> {
    override fun insert(entity: Collection<Any>) {
        super.insert(entity)
    }
    override fun update(entity: Collection<Any>) {
        super.update(entity)
    }
    override fun delete(entity: Collection<Any>) {
        super.delete(entity)
    }
    override fun refresh(entity: Any) {
        super.refresh(entity)
    }
})
//设置类型观察者
ReManager.addObserver(object : ReObserver<ClassInfo> {
    override fun insert(entity: Collection<ClassInfo>) {
        super.insert(entity)
    }
    override fun update(entity: Collection<ClassInfo>) {
        super.update(entity)
    }
    override fun delete(entity: Collection<ClassInfo>) {
        super.delete(entity)
    }
    override fun refresh(entity: ClassInfo) {
        super.refresh(entity)
    }
})
```
---
### API
```kotlin
ReDao.query<QueryClassName> {
    join(JoinClassName::class) {//join操作符 join JoinClassName
        //on JoinClassName.column = QueryClassName.column
        eq(JoinClassName::column, QueryClassName::column)
    }
    where {
        and {//and运算符 where中第一个and、or运算符不生效
            eq(column, xxx)                         // column = xxx
            ne(column, xxx)                         // column <> xxx
            like(column, xxx)                       // column like %xxx%
            likeRight(column, xxx)                  // column like xxx%
            likeLeft(column, xxx)                   // column like %xxx
            notLike(column, xxx)                    // column not like %xxx%
            notLikeRight(column, xxx)               // column not like xxx%
            gt(column, xxx)                         // column > xxx
            ge(column, xxx)                         // column >= xxx
            lt(column, xxx)                         // column < xxx
            le(column, xxx)                         // column <= xxx
            isNull(column)                          // column is null
            isNotNull(column)                       // column is not null
            `in`(column, 1, 2, 3)                   // column in (1,2,3)
            notIn(column, 1, 2, 3)                  // column not in (1,2,3)
            between(column, 1, 10)                  // column between 1 and 10  1<=column<=10
            notBetween(column, 1, 10)               // column not between 1 and 10
            //使用列函数
            //字符串函数
            eq(column, xxx, StringFunctions.UPPER)  //将字符串转换为大写    upper(column) = xxx
            eq(column, xxx, StringFunctions.LOWER)  //将字符串转换为小写    lower(column) = xxx
            eq(column, xxx, StringFunctions.TRIM)   //去除字符串首尾的空格   trim(column) = xxx
            eq(column, xxx, StringFunctions.LENGTH) //返回字符串的长度      length(column) = xxx
            //数值函数
            eq(column, xxx, NumericFunctions.ROUND) //四舍五入 round(column) = xxx
            eq(column, xxx, NumericFunctions.CEIL)  //向上取整 ceil(column) = xxx
            eq(column, xxx, NumericFunctions.FLOOR) //向下取整 floor(column) = xxx
            eq(column, xxx, NumericFunctions.ABS)   //绝对值   abs(column) = xxx
            eq(column, xxx, NumericFunctions.SQRT)  //平方根   sqrt(column) = xxx
        }
        and {//and运算符
            //以下两个条件满足一项
            or {//or运算符
                eq(column, xxx)
            }
            or {//or运算符
                eq(column, xxx)
            }
        }
    }
    groupBy(xxx) {//分组 groupBy xxx
        //分组过滤 having ...
        eq(column, xxx, AggregateFunctions.COUNT)   //总数   count(column) = xxx
        eq(column, xxx, AggregateFunctions.SUM)     //和     sum(column) = xxx
        eq(column, xxx, AggregateFunctions.AVG)     //平均值  avg(column) = xxx
        eq(column, xxx, AggregateFunctions.MIN)     //最小值  min(column) = xxx
        eq(column, xxx, AggregateFunctions.MAX)     //最大值  max(column) = xxx
    }
    //分页相关
    limit(1)
    offset(1)
    //排序相关，可多字段排序
    orderByAsc(column)//升序排序
    orderByDesc(column)//降序排序
}
```
### 添加混淆
```
-keep class com.cjj.re.dao.**{
    public static ** INSTANCE;
}
-keep class com.cjj.re.tableinfo.**{
    public static ** INSTANCE;
}
```
自己项目里面被Entity注解修饰的类也需要不被混淆
```
-keep class packageName.** {*;}  //packageName换成自己项目里的包名
```

