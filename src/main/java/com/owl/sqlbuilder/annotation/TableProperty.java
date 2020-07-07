package com.owl.sqlbuilder.annotation;

import java.lang.annotation.*;

/**
 * 用于添加table的名字
 * @author claire
 * @date 2019-11-11 - 14:28
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableProperty {
    String name() default "";
}
