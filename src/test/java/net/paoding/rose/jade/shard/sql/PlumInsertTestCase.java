/**
 * 
 */
package net.paoding.rose.jade.shard.sql;

import org.springframework.beans.factory.annotation.Autowired;
import net.paoding.rose.jade.shard.sql.dao.UserInfoDAO;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class PlumInsertTestCase extends AbstractTestCase {
	
	
	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetById(){
		userInfoDAO.findById(6);
		userInfoDAO.findById(2);
		userInfoDAO.findById(3);
	}

	/*public void testInsert() {
		UserInfoDO userInfo = createUserInfoDO(102, 30);
		
		Long id = userInfoDAO.save(userInfo);
		
		
		System.out.println(JSON.toJSONString(userInfoDAO.get(id), SerializerFeature.PrettyFormat));
	}
	
	public void testBatchInsert() {
		List<UserInfoDO> userInfos = new ArrayList<UserInfoDO>(5);
		for(int i = 0; i < 5; i++) {
			userInfos.add(createUserInfoDO(103, 40 + i));
		}
		userInfoDAO.save(userInfos);
	}
	
    private static UserInfoDO createUserInfoDO(long group, int age) {
        UserInfoDO userInfo = new UserInfoDO();

        userInfo.setName("耿直PlumInsertTestCase");
        userInfo.setMoney(new BigDecimal("123456"));
        userInfo.setEditable(true);
        userInfo.setGroupId(group);
        userInfo.setAge(age);
        userInfo.setBirthday(DateUtils.addYears(new Date(), -userInfo.getAge()));
        userInfo.setStatus(2);
        userInfo.setCreateTime(new Date());
        userInfo.setEditable(true);
        userInfo.setLastUpdateTime(new Date());

        return userInfo;
    }
    */
	
}
