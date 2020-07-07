package com.owl.sqlbuilder.utils;

import com.owl.sqlbuilder.entity.dto.ResponseColumnMeta;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 结果处理映射父类
 * @author claire
 * @date 2019-11-11 - 10:38
 **/
public class ResultEntityHandler<T> {
    /**
     *  结果
     */
    private List<T> results;
    /**
     * 对象工厂
     */
    protected final ObjectFactory objectFactory;
    /**
     * 映射工厂
     */
    protected final OlapResultReflectorFactory reflectorFactory;
    /**
     * 包装工厂
     */
    protected final ObjectWrapperFactory objectWrapperFactory;

    public ResultEntityHandler(OlapResultReflectorFactory reflectorFactory){
        this.results = new ArrayList<>();
        this.objectFactory = new DefaultObjectFactory();
        this.reflectorFactory = reflectorFactory;
        this.objectWrapperFactory = new DefaultObjectWrapperFactory();

    }

    public void handleCollectionResult(Class<T> clazz, List<ResponseColumnMeta> columnMetas, List<List<String>> values){
    }
    public void handleSingleResult(Class<T> clazz, List<ResponseColumnMeta> columnMetas, List<String> values){
    }
    public List<T> getResults() {
        return results;
    }
}
