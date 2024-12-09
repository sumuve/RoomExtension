package com.cjj.re.base;

import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * room 数据库操作模板基类
 * </p>
 *
 * @author CJJ
 * @since 2024-07-29 14:49
 */
public interface ReBaseCoreDao<T> {


    /** @noinspection unchecked*/
    long[] insert(T... values);

    long[] insert(Collection<T> values);
    /** @noinspection unchecked*/
    int update(T... values);

    int update(Collection<T> values);
    /** @noinspection unchecked*/
    int delete(T... values);

    int delete(Collection<T> values);

    int getNumberByInt(SupportSQLiteQuery sql);

    long getNumberByLong(SupportSQLiteQuery sql);

    float getNumberByFloat(SupportSQLiteQuery sql);

    double getNumberByDouble(SupportSQLiteQuery sql);

    Map<T, Integer> getGroupByInt(SupportSQLiteQuery sql);

    Map<T, Long> getGroupByLong(SupportSQLiteQuery sql);

    Map<T, Float> getGroupByFloat(SupportSQLiteQuery sql);

    Map<T, Double> getGroupByDouble(SupportSQLiteQuery sql);

    List<T> getList(SupportSQLiteQuery sql);
}