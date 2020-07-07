package com.owl.sqlbuilder.utils;

import com.owl.sqlbuilder.entity.dto.ResponseColumnMeta;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;
import java.util.Objects;

/**
 * 结果映射处理类
 * @author claire
 * @date 2019-11-08 - 17:43
 **/
public class OlapResultEntityHandler<T> extends ResultEntityHandler<T> {

    /**
     * 构造方法，进行映射工厂的初始化
     */
    public OlapResultEntityHandler() {
        super(new OlapResultReflectorFactory());
    }

    /**
     * 映射集合类结果
     * @param clazz
     * @param columnMetas
     * @param values
     */
    @Override
    public void handleCollectionResult(Class<T> clazz, List<ResponseColumnMeta> columnMetas, List<List<String>> values) {
        if(Objects.nonNull(values)){
           resolveCollections(clazz, columnMetas, values);
        }
    }

    /**
     * 映射单个结果
     * @param clazz
     * @param columnMetas
     * @param values
     */
    @Override
    public void handleSingleResult(Class<T> clazz,List<ResponseColumnMeta> columnMetas,List<String> values) {
        if(Objects.nonNull(values)){
            resolveSingleEntry(clazz, columnMetas, values);
        }
    }

    /**
     * 处理集合类结果
     * @param clazz
     * @param columnMetas
     * @param values
     */
    private void resolveCollections(Class<T> clazz, List<ResponseColumnMeta> columnMetas, List<List<String>> values){
        super.getResults().clear();
        if(Objects.nonNull(values)){
            values.forEach(value ->{
                super.getResults().add(objectValueMapping(clazz, columnMetas, value));
            });
        }
    }

    /**
     * 处理单个结果
     * @param clazz
     * @param columnMetas
     * @param values
     */
    private void resolveSingleEntry(Class<T> clazz, List<ResponseColumnMeta> columnMetas, List<String> values){
        super.getResults().add(objectValueMapping(clazz, columnMetas, values));
    }

    /**
     * 内容映射
     * @param clazz
     * @param columnMetas
     * @param values
     * @return
     */
    private T objectValueMapping(Class<T> clazz, List<ResponseColumnMeta> columnMetas, List<String> values) {
        T obj = objectFactory.create(clazz);
        ResultReflector reflector = reflectorFactory.findForClass(clazz);
        MetaObject metaObject = MetaObject.forObject(obj, objectFactory, objectWrapperFactory, reflectorFactory);
        if (Objects.nonNull(metaObject)) {
            for (int index = 0; index < columnMetas.size(); index++) {
                String columnName = metaObject.findProperty(columnMetas.get(index).getName(), true);
                Class<?> columnType = metaObject.getGetterType(columnName);
                if (metaObject.hasGetter(columnName)) {
                    if(Objects.nonNull(values.get(index))) {
                        Object reflectedValue = reflector.getReflectedValue(values.get(index), columnType);
                        if (Objects.nonNull(reflectedValue)) {
                            metaObject.setValue(columnName, reflectedValue);
                        }
                    }
                }
            }
        }
        return obj;
    }

}
