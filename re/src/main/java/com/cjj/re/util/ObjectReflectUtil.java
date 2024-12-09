package com.cjj.re.util;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

/**
 * <p>
 * Kotlin object对象反射工具类
 * </p>
 *
 * @author CJJ
 * @since 2024-08-05 14:48
 */
public class ObjectReflectUtil {

    /** @noinspection unchecked*/
    @Nullable
    public static <T> T getInstance(String className) {
        Class<?> kotlinObjectClass = null; //
        try {
            kotlinObjectClass = Class.forName(className);
            Field instanceField = kotlinObjectClass.getDeclaredField("INSTANCE");
//            instanceField.setAccessible(true);
           return (T)instanceField.get(null);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


    }

}
