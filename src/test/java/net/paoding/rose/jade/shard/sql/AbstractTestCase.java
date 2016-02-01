/**
 * 
 */
package net.paoding.rose.jade.shard.sql;

import junit.framework.TestCase;
import net.paoding.rose.jade.shard.sql.dao.CreateTableDAO;
import net.paoding.rose.jade.shard.sql.dao.UserInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import net.paoding.rose.jade.shard.sql.model.UserInfoDO;

import java.math.BigDecimal;
import java.util.Date;

;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public abstract class AbstractTestCase extends TestCase {

    private static final String CONFIG_LOCATION = "junit-test.spring.xml";

    private static ClassPathXmlApplicationContext springContext;

    @Autowired
    private CreateTableDAO createTableDAO;

    @Autowired
    private UserInfoDAO userInfoDAO;

    private static boolean userInfoTable;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("jade.shard.rule.location","shard.xml");
        if (springContext == null) {
            springContext = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        }
        springContext.getAutowireCapableBeanFactory().autowireBean(this);
        if (!userInfoTable) {
            userInfoTable = true;
            createTableDAO.createUserInfoTable(0);
            createTableDAO.createUserInfoTable(1);
            createTableDAO.createUserInfoTable(2);
            createTableDAO.createUserInfoTable(3);
            createTableDAO.createUserInfoTable(4);
            createTableDAO.createUserInfoTable(5);
            //userInfoDAO.save(createUserInfoDO(100, 18));
            //userInfoDAO.save(createUserInfoDO(100, 19));
            //userInfoDAO.save(createUserInfoDO(101, 20));
            //userInfoDAO.save(createUserInfoDO(101, 21));
        }
    }

    private static UserInfoDO createUserInfoDO(long group, int age) {
        UserInfoDO userInfo = new UserInfoDO();

        userInfo.setName("耿直AbstractTestCase");
        userInfo.setMoney(new BigDecimal("120000"));
        userInfo.setEditable(true);
        userInfo.setGroupId(group);
        userInfo.setAge(age);
        //userInfo.setBirthday(DateUtils.addYears(new Date(), userInfo.getAge()));
        userInfo.setStatus(1);
        userInfo.setCreateTime(new Date());
        userInfo.setEditable(true);
        userInfo.setLastUpdateTime(new Date());

        return userInfo;
    }
    
    protected void printJson(Object object) {
    	//System.out.println(JSON.toJSONString(object, true));
    }

}
