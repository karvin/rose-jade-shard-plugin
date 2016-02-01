/**
 * 
 */
package net.paoding.rose.jade.shard.sql.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;
import net.paoding.rose.jade.shard.sql.model.UserInfoDO;

import java.util.List;

/**
 * @author Alan.Geng[gengzhi718@gmail.com]
 *
 */
/*
 * 继承GenericDAO并使用泛型标注对应的实体类型，即拥有了基本的CRUD操作。
 * 你并不需要一个实现类，在Service层中直接使用：
 * @Autowired
 * private UserInfoDAO userInfoDAO;
 * spring框架将自动注入一个自动生成的实现。
 */
@DAO(catalog = "user")
public interface UserInfoDAO {

	@SQL("select * from user_info where group_id=:groupId")
	List<UserInfoDO> findById(@SQLParam("groupId") @ShardBy int userId);
}
