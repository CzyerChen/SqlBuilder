package com.owl.sqlbuilder.client;


import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.google.common.base.CaseFormat;
import com.owl.sqlbuilder.constant.SqlConstant;
import com.owl.sqlbuilder.constant.SqlOperatorEnum;
import com.owl.sqlbuilder.utils.SqlBuilderUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SQL拼接主体类
 *
 * @author claire
 * @date 2019-11-11 - 11:39
 **/
public class SqlScript {
    /**
     * 用于外部访问的SQL
     */
    private String sqlStr;

    private SqlScript(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public static class Builder<T> extends AbstractBuilder<T> {
        /**
         * 用于寄存SQL的String对象
         */
        private StringBuffer sql = new StringBuffer();

        public static final Integer PAIR = 2;

        /**
         * 构造方法，用于父类构造
         *
         * @param entity
         */
        public Builder(T entity) {
            super(entity);
        }

        /**
         * 用于初次添加where 条件
         *
         * @param column
         * @param operatorEnum
         * @param values
         * @return
         */
        public SqlScript.Builder where(String column, SqlOperatorEnum operatorEnum, String... values) {
            if (values.length > 0) {
                this.sql.append(StringPool.SPACE)
                        .append(SqlConstant.WHERE)
                        .append(StringPool.SPACE);
                buildWhereSearchParam(column, operatorEnum, values);
            }
            return this;
        }

        public SqlScript.Builder where(String script) {
            this.sql.append(StringPool.SPACE)
                    .append(SqlConstant.WHERE)
                    .append(StringPool.SPACE)
                    .append(script);
            return this;
        }

        /**
         * 用于再次拼接where条件
         *
         * @param column
         * @param operatorEnum
         * @param values
         * @return
         */
        public SqlScript.Builder andWhere(String column, SqlOperatorEnum operatorEnum, String... values) {
            if (values.length > 0) {
                this.sql.append(StringPool.SPACE)
                        .append(SqlConstant.AND)
                        .append(StringPool.SPACE);
                buildWhereSearchParam(column, operatorEnum, values);
            }
            return this;
        }

        /**
         * 自定义脚本
         *
         * @param script
         * @return
         */
        public SqlScript.Builder andWhere(String script) {
            if (StringUtils.isNotBlank(script)) {
                this.sql.append(StringPool.SPACE)
                        .append(SqlConstant.AND)
                        .append(StringPool.SPACE)
                        .append(script);
            }
            return this;
        }

        /**
         * 用于初次添加select条件，自行提供查询
         *
         * @param columns
         * @return
         */
        public SqlScript.Builder select(List<String> columns) {
            if (CollectionUtils.isNotEmpty(columns)) {
                String selectColumns = String.join(StringPool.COMMA, columns);
                if (StringUtils.isNotBlank(selectColumns)) {
                    this.sql
                            .append(SqlConstant.SELECT)
                            .append(StringPool.SPACE)
                            .append(selectColumns)
                            .append(StringPool.SPACE);
                }
            }
            return this;
        }

        public SqlScript.Builder select(String script) {
            this.sql
                    .append(SqlConstant.SELECT)
                    .append(StringPool.SPACE)
                    .append(script)
                    .append(StringPool.SPACE);
            return this;
        }

        /**
         * 用于初次添加select条件，提供function
         *
         * @param columns
         * @return
         */
        @SafeVarargs
        public final SqlScript.Builder select(SFunction<T, ?>... columns) {
            if (ArrayUtils.isNotEmpty(columns)) {
                this.sql
                        .append(SqlConstant.SELECT)
                        .append(StringPool.SPACE)
                        .append(columnsToString(false, columns))
                        .append(StringPool.SPACE);
            }
            return this;
        }

        public SqlScript.Builder andSelect(String... columns) {
            if (ArrayUtils.isNotEmpty(columns)) {
                this.sql
                        .append(StringPool.COMMA)
                        .append(String.join(StringPool.COMMA, columns))
                        .append(StringPool.SPACE);
            }
            return this;
        }

        public SqlScript.Builder andSelect(String script) {
            this.sql
                    .append(StringPool.COMMA)
                    .append(script)
                    .append(StringPool.SPACE);
            return this;
        }

        /**
         * 用于再次添加select条件，提供SQL操作和列名
         *
         * @param function
         * @param column
         * @return
         */
        public SqlScript.Builder andSelectAlias(String function, String column, String asColumn) {
            if (StringUtils.isBlank(this.sql)) {
                this.sql
                        .append(SqlConstant.SELECT)
                        .append(StringPool.SPACE);
            } else {
                this.sql
                        .append(StringPool.COMMA);
            }
            if (StringUtils.isNotBlank(function)) {
                this.sql
                        .append(function)
                        .append(StringPool.LEFT_BRACKET)
                        .append(column)
                        .append(StringPool.RIGHT_BRACKET);
            } else {
                this.sql.append(column);
            }

            if (StringUtils.isNotBlank(asColumn)) {
                if (SqlBuilderUtils.containsKey(super.entity.getClass().getName())) {
                    Map<String, ColumnCache> columnMap = SqlBuilderUtils.getColumnMap(super.entity.getClass().getName());
                    String currentColumn = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, asColumn);
                    if (columnMap.containsKey(currentColumn)) {
                        this.sql.append(StringPool.SPACE)
                                .append(" AS ")
                                .append(asColumn);
                    }
                }
            } else if (SqlBuilderUtils.containsKey(super.entity.getClass().getName())) {
                Map<String, ColumnCache> columnMap = SqlBuilderUtils.getColumnMap(super.entity.getClass().getName());
                String currentColumn = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
                if (columnMap.containsKey(currentColumn)) {
                    this.sql.append(StringPool.SPACE)
                            .append(" AS ")
                            .append(column);
                }
            }
            return this;
        }

        /**
         * 用于再次添加select条件，提供SQL操作和列function
         *
         * @param function
         * @param column
         * @return
         */
        public SqlScript.Builder andSelect(String function, SFunction<T, ?> column) {
            this.sql
                    .append(StringPool.COMMA)
                    .append(function)
                    .append(StringPool.LEFT_BRACKET)
                    .append(getColumnStr(column))
                    .append(StringPool.RIGHT_BRACKET);
            return this;
        }

        /**
         * 用于null条件
         *
         * @param column
         * @return
         */
        public SqlScript.Builder beNull(String column) {
            if (StringUtils.isNotBlank(column)) {
                this.sql
                        .append(StringPool.SPACE)
                        .append(column)
                        .append(SqlConstant.NULL_OPER);
            }
            return this;
        }

        /**
         * 用于non null 条件
         *
         * @param column
         * @return
         */
        public SqlScript.Builder nonNull(String column) {
            if (StringUtils.isNotBlank(column)) {
                this.sql
                        .append(SqlConstant.AND)
                        .append(StringPool.SPACE)
                        .append(column)
                        .append(StringPool.SPACE)
                        .append(SqlConstant.NON_NULL_OPER);
            }
            return this;
        }

        public SqlScript.Builder nonNull(String... column) {
            if (ArrayUtils.isNotEmpty(column)) {
                Arrays.stream(column).forEach(c -> {
                    this.sql
                            .append(SqlConstant.AND)
                            .append(StringPool.SPACE)
                            .append(c)
                            .append(StringPool.SPACE)
                            .append(SqlConstant.NON_NULL_OPER);
                });
            }
            return this;
        }

        /**
         * 用于添加from条件
         *
         * @param tableName
         * @return
         */
        public SqlScript.Builder from(String tableName) {
            if (StringUtils.isNotBlank(tableName)) {
                this.sql
                        .append(StringPool.SPACE)
                        .append(SqlConstant.FROM)
                        .append(StringPool.SPACE)
                        .append(tableName)
                        .append(StringPool.SPACE);
            } else {
                throw new RuntimeException( "缺失表名");
            }
            return this;
        }

        public SqlScript.Builder leftJoin(String subTableName, String onScript) {
            if (StringUtils.isNotBlank(subTableName) && StringUtils.isNotBlank(onScript)) {
                this.sql
                        .append(StringPool.SPACE)
                        .append(SqlConstant.LEFT_JOIN)
                        .append(StringPool.SPACE)
                        .append(subTableName)
                        .append(StringPool.SPACE)
                        .append(SqlConstant.JOIN_ON)
                        .append(StringPool.SPACE)
                        .append(onScript)
                        .append(StringPool.SPACE);
            } else {
                throw new RuntimeException( "缺失表名");
            }
            return this;
        }


        /**
         * 用于添加自定义脚本
         *
         * @param script
         * @return
         */
        public SqlScript.Builder subScript(String script) {
            if (StringUtils.isNotBlank(script)) {
                this.sql.append(script);
            }
            return this;
        }

        /**
         * 用于添加group by
         *
         * @param columns
         * @return
         */
        public SqlScript.Builder groupBy(String... columns) {
            if (ArrayUtils.isNotEmpty(columns)) {
                this.sql.append(StringPool.SPACE)
                        .append(SqlConstant.GROUP_BY)
                        .append(StringPool.SPACE)
                        .append(String.join(StringPool.COMMA, columns))
                        .append(StringPool.SPACE);
            }
            return this;
        }

        /**
         * 用于添加order by
         *
         * @param columns
         * @return
         */
        public SqlScript.Builder orderBy(String... columns) {
            if (ArrayUtils.isNotEmpty(columns)) {
                this.sql.append(StringPool.SPACE)
                        .append(SqlConstant.ORDER_BY)
                        .append(StringPool.SPACE)
                        .append(String.join(StringPool.COMMA, columns))
                        .append(StringPool.SPACE);
            }
            return this;
        }

        /**
         * 用于添加limit限制
         *
         * @param limit
         * @return
         */
        public SqlScript.Builder limit(Long limit){
            if(Objects.nonNull(limit)){
                this.sql.append(SqlConstant.LIMIT)
                        .append(limit);
            }else{
                this.sql.append(SqlConstant.LIMIT)
                        .append(SqlConstant.LIMIT_DEFAULT);
            }
            return this;
        }

        /**
         * 用于生成SqlScript对象
         *
         * @return
         */
        public SqlScript build() {
            return new SqlScript(this.sql.toString());
        }

        /**
         * 获取column名称
         *
         * @param column
         * @return
         */
        public String getColumnStr(SFunction<T, ?> column) {
            return columnsToString(false, column);
        }

        /**
         * 用于拼接where条件
         *
         * @param column
         * @param operatorEnum
         * @param values
         */
        private void buildWhereSearchParam(String column, SqlOperatorEnum operatorEnum, String... values) {
            String[] params = foldParmaValues(values);
            switch (operatorEnum) {
                //等于
                case EQUAL:
                    processWhereConditionsEqual(column, params, values);
                    break;
                case NOT_EQUAL:
                    processWhereConditionNotEqual(column, params, values);
                    break;
                //大于等于，小于等于
                case RANGE_INCLUDE:
                    processWhereConditionRangeBetween(column, params, values);
                    break;
                //大于，小于
                case RANGE_EXCLUDE:
                    processWhereConditionRangeExclude(column, params, values);
                    break;
                //大于等于，小于
                case RANGE_INCLUDE_LEFT:
                    processWhereConditionRangeLeftInclude(column, params, values);
                    break;
                //大于，小于等于
                case RANGE_INCLUDE_RIGHT:
                    processWhereConditionRangeRightInclude(column, params, values);
                    break;
                //大于
                case GREATER_THAN:
                    processWhereConditionGreaterThan(column, params, values);
                    break;
                //小于
                case LESS_THAN:
                    processWhereConditionLessThan(column, params, values);
                    break;
                //大于等于
                case GREATER_THAN_INCLUDE:
                    processWhereConditionGreaterThanInclude(column, params, values);
                    break;
                //小于等于
                case LESS_THAN_INCLUDE:
                    processWhereConditionLessThanInclude(column, params, values);
                    break;
                default:
            }
        }

        /**
         * 为参数添加单引号
         *
         * @param values
         * @return
         */
        private String[] foldParmaValues(String... values) {
            String[] newValues = new String[values.length];
            for (int index = 0; index < values.length; index++) {
                newValues[index] = "\'" + values[index] + "\'";
            }
            return newValues;
        }

        /**
         * 功能描述: <br/>
         * 〈处理等于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionsEqual(String column, String[] params, String... values) {
            if (values.length > 1) {
                this.sql.append(column)
                        .append(StringPool.SPACE)
                        .append(SqlConstant.IN)
                        .append(StringPool.LEFT_BRACKET)
                        .append(String.join(StringPool.COMMA, params))
                        .append(StringPool.RIGHT_BRACKET)
                        .append(StringPool.SPACE);
            } else {
                this.sql.append(column)
                        .append(StringPool.EQUALS)
                        .append(params[0])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理不等于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionNotEqual(String column, String[] params, String... values) {
            if (values.length == 1) {
                this.sql.append(column)
                        .append(StringPool.EXCLAMATION_MARK)
                        .append(StringPool.EQUALS)
                        .append(params[0])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理大于等于小于等于范围条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionRangeBetween(String column, String[] params, String... values) {
            if (values.length == PAIR) {
                this.sql.append(column)
                        .append(StringPool.RIGHT_CHEV)
                        .append(StringPool.EQUALS)
                        .append(params[0])
                        .append(StringPool.SPACE)
                        .append(SqlConstant.AND)
                        .append(StringPool.SPACE)
                        .append(column)
                        .append(StringPool.LEFT_CHEV)
                        .append(StringPool.EQUALS)
                        .append(params[1])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理大于小于不包括等于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionRangeExclude(String column, String[] params, String... values) {
            if (values.length == PAIR) {
                this.sql.append(column)
                        .append(StringPool.RIGHT_CHEV)
                        .append(params[0])
                        .append(StringPool.SPACE)
                        .append(SqlConstant.AND)
                        .append(StringPool.SPACE)
                        .append(column)
                        .append(StringPool.LEFT_CHEV)
                        .append(params[1])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理大于等于小于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionRangeLeftInclude(String column, String[] params, String... values) {
            if (values.length == PAIR) {
                this.sql.append(column)
                        .append(StringPool.RIGHT_CHEV)
                        .append(StringPool.EQUALS)
                        .append(params[0])
                        .append(StringPool.SPACE)
                        .append(SqlConstant.AND)
                        .append(StringPool.SPACE)
                        .append(column)
                        .append(StringPool.LEFT_CHEV)
                        .append(params[1])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理大于小于等于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionRangeRightInclude(String column, String[] params, String... values) {
            if (values.length == PAIR) {
                this.sql.append(column)
                        .append(StringPool.RIGHT_CHEV)
                        .append(params[0])
                        .append(StringPool.SPACE)
                        .append(SqlConstant.AND)
                        .append(StringPool.SPACE)
                        .append(column)
                        .append(StringPool.LEFT_CHEV)
                        .append(StringPool.EQUALS)
                        .append(params[1])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理大于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionGreaterThan(String column, String[] params, String... values) {
            if (values.length == 1) {
                this.sql.append(column)
                        .append(StringPool.RIGHT_CHEV)
                        .append(params[0])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理小于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionLessThan(String column, String[] params, String... values) {
            if (values.length == 1) {
                this.sql.append(column)
                        .append(StringPool.LEFT_CHEV)
                        .append(params[0])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理大于等于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionGreaterThanInclude(String column, String[] params, String... values) {
            if (values.length == 1) {
                this.sql.append(column)
                        .append(StringPool.RIGHT_CHEV)
                        .append(StringPool.EQUALS)
                        .append(params[0])
                        .append(StringPool.SPACE);
            }
        }

        /**
         * 功能描述: <br/>
         * 〈处理小于等于条件〉
         *
         * @param column
         * @param params
         * @param values
         * @author claire
         * @date 2020-02-10 - 22:30
         * @since 1.3.1
         */
        private void processWhereConditionLessThanInclude(String column, String[] params, String... values) {
            if (values.length == 1) {
                this.sql.append(column)
                        .append(StringPool.LEFT_CHEV)
                        .append(StringPool.EQUALS)
                        .append(params[0])
                        .append(StringPool.SPACE);
            }
        }
    }

    /**
     * 获取最终SQL值
     *
     * @return
     */
    public String getSqlStr() {
        return sqlStr;
    }
}
