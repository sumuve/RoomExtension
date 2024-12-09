package com.cjj.re.annotation

/**
 * <p>
 *
 * </p>
 *
 * @author CJJ
 * @since 2024-06-24 08:58
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class JoinProperty(
    val parentColumn: String = "",
    val entityColumn: String = "",
)
