/**
 * Author:   claire
 * Date:    2020-07-07 - 13:59
 * Description: person 类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-07-07 - 13:59          V1.4.0           person 类
 */
package com.owl.sqlbuilder.entity;

import com.owl.sqlbuilder.annotation.TableProperty;

/**
 * 功能简述 <br/> 
 * 〈person 类〉
 *
 * @author claire
 * @date 2020-07-07 - 13:59
 * @since 1.4.0
 */
@TableProperty(name = "#{tableNameConfig.person}")
public class Person {

    private String id;
    private String name;
    private Boolean sex;
    private String address;

    public Person() {
    }

    public Person(String id, String name, Boolean sex, String address) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
