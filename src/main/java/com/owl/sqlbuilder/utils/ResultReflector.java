package com.owl.sqlbuilder.utils;

import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.Reflector;

import java.util.List;
import java.util.Map;

/**
 * 结果映射
 * @author claire
 * @date 2019-11-08 - 15:48
 **/
public class ResultReflector<T> extends Reflector {
    private static final String UNKNOWN_NULL = "null";

    /**
     * 构造方法
     * @param clazz
     */
    public ResultReflector(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 获取映射的类型
     * @param value
     * @param type
     * @return
     */
    public Object getReflectedValue( String value, Class<T> type) {
        if (type.isAssignableFrom( Map.class)) {
            return null;
        } else {
            if (type.isAssignableFrom( List.class)) {
                return null;
            } else if (type.isAssignableFrom(Object.class)) {
                return value;
            } else if(type.isAssignableFrom(String.class)){
                return value;
            } else if (type.isAssignableFrom(char[].class)) {
                return value.toCharArray();
            } else if (type.isAssignableFrom(Boolean.class)) {
                return Boolean.valueOf(value);
            } else if (type.isAssignableFrom(Byte[].class)) {
                return value.getBytes();
            } else if (type.isAssignableFrom(Double.class)) {
                return Double.valueOf(value);
            } else if (type.isAssignableFrom(Float.class)) {
                return Float.valueOf(value);
            } else if (type.isAssignableFrom(Integer.class)) {
                if(UNKNOWN_NULL.equals(value)){
                    return null;
                }else {
                    return Integer.valueOf(value);
                }
            } else if (type.isAssignableFrom(Long.class)) {
                return Long.valueOf(value);
            } else if (type.isAssignableFrom(Short.class)) {
                return Short.valueOf(value);
            } else {
                throw new ReflectionException("The 'prop.getName()" + "' properties of " + value + " is not a List or Array.");
            }
        }
    }


}
