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
public class PlumUpdateTestCase extends AbstractTestCase {

	@Autowired
	private UserInfoDAO purchaseContractDAO;
	
	/*public void testUpdate() {
		UserInfoDO contract = new UserInfoDO();
		
		contract.setId(1L);
		contract.setLastUpdateTime(new Date());
		contract.setName("Alan.Geng");
		
		System.out.println(purchaseContractDAO.update(contract));
		
		UserInfoDO user = purchaseContractDAO.get(1L);
		System.out.println(JSON.toJSONString(user, SerializerFeature.PrettyFormat));
	}
	
	public void testSpecialUpdate() {
		purchaseContractDAO.updateByGroup(null, 25, 100);
		
		List<UserInfoDO> updated = purchaseContractDAO.findByGroupId(100L);
		System.out.println(JSON.toJSONString(updated, SerializerFeature.PrettyFormat));
	}
	
	public void testSpecialInUpdate() {
		List<Integer> groupIds = new ArrayList<Integer>();
		groupIds.add(100);
		groupIds.add(101);
		
		purchaseContractDAO.updateByGroupIds("Alan.Geng", 29, groupIds);
		
		List<UserInfoDO> updated = purchaseContractDAO.findByGroupIds(groupIds);
		System.out.println(JSON.toJSONString(updated, SerializerFeature.PrettyFormat));
	}*/
}
