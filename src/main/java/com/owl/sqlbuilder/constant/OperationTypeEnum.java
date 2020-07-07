/**
 * Author:   claire
 * Date:    2020-07-07 - 14:01
 * Description: 操作常量类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-07-07 - 14:01          V1.4.0           操作常量类
 */
package com.owl.sqlbuilder.constant;

/**
 * 功能简述 <br/> 
 * 〈操作常量类〉
 *
 * @author claire
 * @date 2020-07-07 - 14:01
 * @since 1.4.0
 */
public enum OperationTypeEnum {
    /**
     * 无操作
     */
    OPER_NULL("");

    private String type;

    OperationTypeEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
