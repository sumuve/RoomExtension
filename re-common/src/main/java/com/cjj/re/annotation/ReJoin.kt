package com.cjj.re.annotation


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class ReJoin(
    val joinProperty: String = "",
    val joinProperties: Array<JoinProperty> = []
)
