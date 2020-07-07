/**
 * Author:   claire
 * Date:    2020-07-07 - 14:08
 * Description: 表名配置类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-07-07 - 14:08          V1.4.0           表名配置类
 */
package com.owl.sqlbuilder.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 功能简述 <br/> 
 * 〈表名配置类〉
 *
 * @author claire
 * @date 2020-07-07 - 14:08
 * @since 1.4.0
 */
@Configuration
@ConfigurationProperties(prefix = "tablename")
public class TableNameConfig {
    public String person;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public TableNameConfig(String person) {
        this.person = person;
    }

    public TableNameConfig() {
    }
}
