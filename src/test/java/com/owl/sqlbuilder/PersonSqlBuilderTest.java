/**
 * Author:   claire
 * Date:    2020-07-07 - 14:10
 * Description: 测试类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-07-07 - 14:10          V1.4.0           测试类
 */
package com.owl.sqlbuilder;

import com.alibaba.fastjson.JSON;
import com.owl.sqlbuilder.client.SqlScript;
import com.owl.sqlbuilder.config.TableNameConfig;
import com.owl.sqlbuilder.entity.Person;
import com.owl.sqlbuilder.entity.dto.ResultEntity;
import com.owl.sqlbuilder.utils.OlapResultEntityHandler;
import com.owl.sqlbuilder.utils.SqlBuilderUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能简述 <br/>
 * 〈测试类〉
 *
 * @author claire
 * @date 2020-07-07 - 14:10
 * @since 1.4.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SqlBuilderApplication.class)
@ActiveProfiles("dev")
public class PersonSqlBuilderTest {
    private static final Logger log = LoggerFactory.getLogger(PersonSqlBuilderTest.class);

    @Test
    public void testBuildSql() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SqlBuilderUtils.setTableNameMap(Person.class,SqlBuilderUtils.getTableNameInitial(Person.class));
        //建造者模式 Builder
        SqlScript.Builder<Person> builder = new SqlScript.Builder<Person>(new Person());
        SqlScript script = builder
                .select(Person::getName, Person::getAddress)
                .from(SqlBuilderUtils.getTableName(Person.class))
                .where("1=1")
                .groupBy(builder.getColumnStr(Person::getName))
                .build();
        String sqlStr = script.getSqlStr();
        log.info(sqlStr);
    }

    @Test
    public void testParseContent() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SqlBuilderUtils.setTableNameMap(Person.class,SqlBuilderUtils.getTableNameInitial(Person.class));
        //some examples for olap query result
        //we can user this to convert nosql to sql
        String content = "{\"columnMetas\":[{\"isNullable\":1,\"displaySize\":256,\"label\":\"ID\",\"name\":\"ID\",\"schemaName\":\"DEFAULT\",\"catelogName\":null,\"tableName\":\"TEST_PERSON\",\"precision\":256,\"scale\":0,\"columnType\":4,\"columnTypeName\":\"VARCHAR\",\"signed\":true,\"autoIncrement\":false,\"caseSensitive\":true,\"searchable\":false,\"currency\":false,\"definitelyWritable\":false,\"writable\":false,\"readOnly\":true},{\"isNullable\":1,\"displaySize\":256,\"label\":\"NAME\",\"name\":\"NAME\",\"schemaName\":\"DEFAULT\",\"catelogName\":null,\"tableName\":\"TEST_PERSON\",\"precision\":256,\"scale\":0,\"columnType\":4,\"columnTypeName\":\"VARCHAR\",\"signed\":true,\"autoIncrement\":false,\"caseSensitive\":true,\"searchable\":false,\"currency\":false,\"definitelyWritable\":false,\"writable\":false,\"readOnly\":true},{\"isNullable\":1,\"displaySize\":256,\"label\":\"SEX\",\"name\":\"SEX\",\"schemaName\":\"DEFAULT\",\"catelogName\":null,\"tableName\":\"TEST_PERSON\",\"precision\":256,\"scale\":0,\"columnType\":4,\"columnTypeName\":\"INTEGER\",\"signed\":true,\"autoIncrement\":false,\"caseSensitive\":true,\"searchable\":false,\"currency\":false,\"definitelyWritable\":false,\"writable\":false,\"readOnly\":true},{\"isNullable\":1,\"displaySize\":256,\"label\":\"ADDRESS\",\"name\":\"ADDRESS\",\"schemaName\":\"DEFAULT\",\"catelogName\":null,\"tableName\":\"TEST_PERSON\",\"precision\":256,\"scale\":0,\"columnType\":4,\"columnTypeName\":\"VARCHAR\",\"signed\":true,\"autoIncrement\":false,\"caseSensitive\":true,\"searchable\":false,\"currency\":false,\"definitelyWritable\":false,\"writable\":false,\"readOnly\":true}],\"results\":[[8,\"lily\",0,\"淮安市\"]],\"cube\":\"CUBE[name=person_cube]\",\"affectedRowCount\":0,\"isException\":false,\"exceptionMessage\":null,\"duration\":51,\"totalScanCount\":2,\"totalScanBytes\":100,\"hitExceptionCache\":false,\"storageCacheUsed\":false,\"traceUrl\":null,\"pushDown\":false,\"partial\":false}";
        ResultEntity resultEntity = JSON.parseObject(content, ResultEntity.class);
        OlapResultEntityHandler<Person> entityHandler = new OlapResultEntityHandler<>();
        entityHandler.handleCollectionResult(Person.class,resultEntity.getColumnMetas(),resultEntity.getResults());
        List<Person> results = entityHandler.getResults();
        Assert.assertNotNull(results);

    }

}
