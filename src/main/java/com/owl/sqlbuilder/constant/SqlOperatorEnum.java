package com.owl.sqlbuilder.constant;

/**
 * SQL操作枚举类
 * @author claire
 * @date 2019-11-11 - 17:37
 **/
public enum SqlOperatorEnum {
    /**
     * =
     */
    EQUAL,
    /**
     * [a,b]
     */
    RANGE_INCLUDE,
    /**
     * (a,b)
     */
    RANGE_EXCLUDE,
    /**
     *[a,b)
     */
    RANGE_INCLUDE_LEFT,
    /**
     *(a,b]
     */
    RANGE_INCLUDE_RIGHT,
    /**
     * (a,....)
     */
    GREATER_THAN,
    /**
     * (...,a)
     */
    LESS_THAN,
    /**
     * [a,...)
     */
    GREATER_THAN_INCLUDE,
    /**
     *(...,a]
     */
    LESS_THAN_INCLUDE,
    /**
     * !=
     */
    NOT_EQUAL;
}
