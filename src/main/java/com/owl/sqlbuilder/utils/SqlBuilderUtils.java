package com.owl.sqlbuilder.utils;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.google.common.base.CaseFormat;
import com.owl.sqlbuilder.annotation.TableProperty;
import com.owl.sqlbuilder.constant.OperationTypeEnum;
import com.owl.sqlbuilder.entity.Person;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于存储对象column的缓存
 *
 * @author claire
 * @date 2019-11-11 - 17:04
 **/
public class SqlBuilderUtils {
    private static final Logger log = LoggerFactory.getLogger(SqlBuilderUtils.class);
    /**
     * 字段映射缓存
     */
    private static final Map<String, Map<String, ColumnCache>> SQL_BUILDER_MAP = new ConcurrentHashMap<>(16);
    private static final Map<Class, String> SELF_DEFINE_TABLE_NAME_MAP = new ConcurrentHashMap<>(16);
    /**
     * annotation & columncache
     **/
    private static final Map<String, ColumnCache> META_TAG_TYPE_COLUMN_MAPPING = new ConcurrentHashMap<>(16);


    static {
        initMetaTagsColumns();
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static Map<String, ColumnCache> getColumnMap(String key) {
        return SQL_BUILDER_MAP.getOrDefault(key, Collections.emptyMap());
    }

    public static Map<String, ColumnCache> getAndCacheColumnMap(Class clazz) {
        Map<String, ColumnCache> columnCacheMap = getColumnMap(clazz.getName());
        if (!columnCacheMap.isEmpty()) {
            return columnCacheMap;
        }
        Map<String, ColumnCache> columnMap = new HashMap<>(16);
        Field[] declaredFields = clazz.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(declaredFields)) {
            Arrays.stream(declaredFields).forEach(c -> {
                String name = c.getName();
                String name1 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
                ColumnCache columnCache = new ColumnCache(c.getName(), name1);
                columnMap.put(c.getName(), columnCache);
            });
        }
        if (!columnMap.isEmpty()) {
            setColumnMap(clazz.getName(), columnMap);
        }
        return columnMap;
    }

    /**
     * 设置缓存
     *
     * @param entityName
     * @param cacheMap
     */
    public static void setColumnMap(String entityName, Map<String, ColumnCache> cacheMap) {
        SQL_BUILDER_MAP.put(entityName, cacheMap);
    }

    public static void setTableNameMap(Class clazz, String tableName) {
        SELF_DEFINE_TABLE_NAME_MAP.put(clazz, tableName);
    }

    /**
     * 判断是否包含
     *
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        return SQL_BUILDER_MAP.containsKey(key);
    }

    /**
     * 获取表名缓存
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class clazz) {
        return SELF_DEFINE_TABLE_NAME_MAP.getOrDefault(clazz, StringUtils.EMPTY);
    }

    public static ColumnCache getTagColumnNameByType(String type) {
        return META_TAG_TYPE_COLUMN_MAPPING.getOrDefault(type, null);
    }


    /**
     * 获取表名
     *
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static String getTableNameInitial(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String preName = "";
        Annotation[] annotations = clazz.getAnnotations();
        if (ArrayUtils.isNotEmpty(annotations)) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().isAssignableFrom(TableProperty.class)) {
                    TableProperty property = (TableProperty) annotation;
                    preName = property.name();
                }
            }
        }
        SpelExpressionParser parser = new SpelExpressionParser();
        if (StringUtils.isNotBlank(preName) && preName.contains(ParserContext.TEMPLATE_EXPRESSION.getExpressionPrefix())) {
            Expression expression = parser.parseExpression(preName, ParserContext.TEMPLATE_EXPRESSION);
            String expressionString = expression.getExpressionString();
            String[] str = expressionString.split("\\.");
            String className = str[0];
            String firstChar = str[1].substring(0, 1);
            String rest = str[1].substring(1, str[1].length());
            String methodName = "get" + firstChar.toUpperCase() + rest;

            Object bean = SpringContextUtil.getBean(className);
            Class<?> aClass = bean.getClass();
            Method method = aClass.getMethod(methodName);
            String value = (String) method.invoke(bean);
            log.info("APP INIT", value);
            return value;
        } else {
            return preName;
        }
    }

    private static void initMetaTagsColumns() {
        Class<?>[] entityClasses = new Class<?>[1];
        entityClasses[0] = Person.class;
        Arrays.stream(entityClasses).forEach(entityClass -> {
            Field[] declaredFields = entityClass.getDeclaredFields();
            if (ArrayUtils.isNotEmpty(declaredFields)) {
                Arrays.stream(declaredFields).forEach(c -> {
                    String name = c.getName();
                    String name1 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
                    ColumnCache columnCache = new ColumnCache(c.getName(), name1);
                });
            }
        });
    }


}
