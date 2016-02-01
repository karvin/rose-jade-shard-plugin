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

@DAO(catalog = "user")
public interface CreateTableDAO {

    String create_user_info_table = "create table user_info "
                                    + "(id bigint IDENTITY not null primary key"
                                    + ",name varchar(200) null "
                                    + ",group_id bigint not null"
                                    + ",birthday datetime not null" + ",age int not null"
                                    + ",money decimal(20,2) not null"
                                    + ",create_time datetime not null"
                                    + ",last_update_time timestamp not null"
                                    + ",status int not null"
                                    + ",editable int not null" + ");";

    @SQL(create_user_info_table)
    public void createUserInfoTable(@SQLParam("index") @ShardBy int db);


    @SQL("select * from user_info where group_id=:groupId")
    public List<UserInfoDO> findByGroup(@SQLParam("groupId") @ShardBy int groupId);
}
