package com.cjj.re.compiler.template

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile

class DaoUtilClassTemplate(
    env: SymbolProcessorEnvironment,
    kFile: KSFile
) :
    ClassTemplate(env, kFile) {
    override val className: String = "DaoUtil"
    override val packageName: String = "com.cjj.re.util"
    override val extensionName: String = "java"

    override fun getImportList(): List<String> = arrayListOf(
        "androidx.room.RoomDatabase",
        "com.cjj.re.ReManager",
        "com.cjj.re.dao.core.ReCoreDao",
        "java.lang.reflect.InvocationTargetException",
        "java.lang.reflect.Method"
    )

    override fun getClassTemplate(sb: StringBuilder) {
        sb.append(
            """
            public class DaoUtil {

                public static class Instance {
                    public static ReCoreDao dao;

                    static {
                        RoomDatabase database = ReManager.INSTANCE.getDatabase();
                        Class<? extends RoomDatabase> aClass = database.getClass();
                        try {
                            Method method = aClass.getMethod("getReCoreDao");
                            dao = (ReCoreDao) method.invoke(database);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        """.trimIndent()
        )
    }
}
