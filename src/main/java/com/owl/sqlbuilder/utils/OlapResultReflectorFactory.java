package com.owl.sqlbuilder.utils;

import org.apache.ibatis.reflection.ReflectorFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 结果处理映射工厂，主要用于产生映射类
 *
 * @author claire
 * @date 2019-11-08 - 15:44
 **/
public class OlapResultReflectorFactory implements ReflectorFactory {

    private boolean classCacheEnabled = true;
    private final ConcurrentMap<Class<?>, ResultReflector> reflectorMap = new ConcurrentHashMap<>();

    /***
     * default
     * @return
     */
    @Override
    public boolean isClassCacheEnabled() {
        return this.classCacheEnabled;
    }

    /**
     *  default
     * @param classCacheEnabled
     */
    @Override
    public void setClassCacheEnabled(boolean classCacheEnabled) {
        this.classCacheEnabled = classCacheEnabled;
    }

    /**
     * 获取对应类的映射类
     * @param type
     * @return
     */
    @Override
    public ResultReflector findForClass(Class<?> type) {
        return this.classCacheEnabled ? (ResultReflector)this.reflectorMap.computeIfAbsent(type, ResultReflector::new) : new ResultReflector(type);
    }
}
