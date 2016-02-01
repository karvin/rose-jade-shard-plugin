/**
 * 
 */
package net.paoding.rose.jade.shard.sql;

import net.paoding.rose.jade.shard.sql.dao.UserInfoDAO;

import javax.annotation.Resource;

/**
 * @author Alan
 *
 */
public class PlumDeleteTestCase extends AbstractTestCase {
	
	@Resource
	private UserInfoDAO userInfoDAO;

	/*public void testDelete() {
		userInfoDAO.delete(0l);
	}
	
	public void testDeleteByGroupId() {
		userInfoDAO.deleteByGroupId(100);
		printJson(userInfoDAO.findByNameLike("耿直%"));
	}*/
}
