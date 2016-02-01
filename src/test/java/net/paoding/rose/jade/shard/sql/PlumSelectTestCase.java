/**
 * 
 */
package net.paoding.rose.jade.shard.sql;

import net.paoding.rose.jade.shard.sql.dao.CreateTableDAO;
import net.paoding.rose.jade.shard.sql.dao.UserInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
public class PlumSelectTestCase extends AbstractTestCase {
	
	@Autowired
	private UserInfoDAO userInfoDAO;

	@Autowired
	private CreateTableDAO createTableDAO;

	/*public void testGetById() {
		UserInfoDO userInfoDO = userInfoDAO.get(2L);
		
		assert userInfoDO != null;
		System.out.println(JSON.toJSONString(userInfoDO, SerializerFeature.PrettyFormat));
	}
	
	public void testFindByGroupId() {
		List<UserInfoDO> userInfos = userInfoDAO.findByGroupId(100L);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testFindByNameLike() {
		List<UserInfoDO> userInfos = userInfoDAO.findByNameLike("耿%");
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testFindByIdBetween() {
		List<UserInfoDO> userInfos = userInfoDAO.findByIdBetween(0, 9);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testGetOrderBy() {
		List<UserInfoDO> userInfos = userInfoDAO.findByNameLikeWithOrder(//
				"耿%", Plum.asc("id", "createTime").desc("lastUpdateTime"));
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}
	
	public void testGetRange() {
		List<UserInfoDO> userInfos = userInfoDAO.findByNameWithLimit("耿%", 0, 2);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}

	public void testShard(){
		List<UserInfoDO> userInfos = createTableDAO.findByGroup(1);
		System.out.println(JSON.toJSONString(userInfos,SerializerFeature.PrettyFormat));
	}
	
	public void testFindByGroupIds() {
		List<Integer> groupIds = new ArrayList<Integer>();
		groupIds.add(100);
		groupIds.add(101);
		
		List<UserInfoDO> userInfos = userInfoDAO.findByGroupIds(groupIds);
		
		System.out.println(JSON.toJSONString(userInfos, SerializerFeature.PrettyFormat));
	}*/
	
}
