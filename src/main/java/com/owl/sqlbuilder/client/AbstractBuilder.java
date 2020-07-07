package com.owl.sqlbuilder.client;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.google.common.base.CaseFormat;
import com.owl.sqlbuilder.utils.SqlBuilderUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * 抽象SQL构建类，主要负责维护对象变量
 *
 * @author claire
 * @date 2019-11-11 - 17:00
 **/
public class AbstractBuilder<T> {
    protected T entity;
    private Class<T> entityClass;
    private Map<String, ColumnCache> columnMap = null;
    private boolean initColumnMap = true;

    /**
     * 构造方法，进行初始化
     * @param entity
     */
    AbstractBuilder(T entity) {
        this.entity = entity;
        this.initEntityClass();
        if (initColumnMap && entityClass != null) {
            initColumnCache();
            initColumnMap = true;
        }
    }

    /**
     * 初始化对象类
     */
    private void initEntityClass() {
        if (this.entityClass == null && this.entity != null) {
            this.entityClass = (Class<T>) entity.getClass();
        }
    }


    /**
     * 初始化对象列缓存
     */
    private void initColumnCache() {
        if (SqlBuilderUtils.containsKey(entityClass.getName())) {
            Map<String, ColumnCache> columnMap = SqlBuilderUtils.getColumnMap(entityClass.getName());
            if (this.columnMap==null && columnMap != null) {
                this.columnMap = columnMap;
            }
        } else {
            if (this.columnMap == null) {
                this.columnMap = new HashMap<>(16);
                Field[] declaredFields = entityClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(declaredFields)) {
                    Arrays.stream(declaredFields).forEach(c -> {
                        String name = c.getName();
                        String name1 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
                        ColumnCache columnCache = new ColumnCache(c.getName(), name1);
                        this.columnMap.put(c.getName(), columnCache);
                    });
                }
            }
            if (columnMap != null) {
                SqlBuilderUtils.setColumnMap(entityClass.getName(), columnMap);
            }
        }
    }

    /**
     * 获取类的列名
     * @param onlyColumn
     * @param columns
     * @return
     */
    protected String columnsToString(boolean onlyColumn, SFunction<T, ?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(i, onlyColumn)).collect(joining(StringPool.COMMA));
    }

    /**
     * 获取类的列名
     * @param column
     * @param onlyColumn
     * @return
     */
    protected String columnToString(SFunction<T, ?> column, boolean onlyColumn) {
        return getColumn(LambdaUtils.resolve(column), onlyColumn);
    }

    /**
     * 从初始化中获取列名
     * @param lambda
     * @param onlyColumn
     * @return
     * @throws MybatisPlusException
     */
    private String getColumn(SerializedLambda lambda, boolean onlyColumn) throws MybatisPlusException {
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        Class aClass = lambda.getInstantiatedMethodType();
        if (!initColumnMap) {
            initColumnCache();
        }
        Assert.notNull(columnMap, "can not find lambda cache for this entity [%s]", aClass.getName());
        ColumnCache columnCache = columnMap.get(fieldName);
        Assert.notNull(columnCache, "can not find lambda cache for this property [%s] of entity [%s]",
                fieldName, aClass.getName());
        return onlyColumn ? columnCache.getColumn() : columnCache.getColumnSelect();
    }


}
