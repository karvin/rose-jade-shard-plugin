package net.paoding.rose.jade.shard.datasource;

import net.paoding.rose.jade.annotation.ShardBy;
import net.paoding.rose.jade.dataaccess.DataSourceFactory;
import net.paoding.rose.jade.dataaccess.DataSourceHolder;
import net.paoding.rose.jade.shard.interceptor.ShardInterpreter;
import net.paoding.rose.jade.shard.rule.Rule;
import net.paoding.rose.jade.statement.StatementMetaData;

import java.util.List;
import java.util.Map;

/**
 * Created by karvin on 16/1/29.
 */
public class ShardDataSourceFactory implements DataSourceFactory {

    private List<DataSourceFactory> dataSources;
    private List<Rule> rules;

    public ShardDataSourceFactory(List<DataSourceFactory> dataSources,List<Rule> rules){
        if(rules == null || dataSources == null){
            throw new IllegalArgumentException("dataSources and rules should pair");
        }
        if(dataSources.size() != rules.size()){
            throw new IllegalArgumentException("dataSources and rules size not equals");
        }
        this.dataSources = dataSources;
        this.rules = rules;
    }

    public DataSourceHolder getHolder(StatementMetaData statementMetaData, Map<String, Object> map) {
        ShardBy shardBy = statementMetaData.getShardBy();
        if(shardBy == null){
            return this.dataSources.get(0).getHolder(statementMetaData,map);
        }
        int shardByIndex = statementMetaData.getShardByIndex();
        DataSourceFactory dataSourceFactory = this.selectDataSourceFactory(map,shardByIndex,shardBy);
        if(dataSourceFactory == null){
            return null;
        }
        return dataSourceFactory.getHolder(statementMetaData,map);
    }

    private DataSourceFactory selectDataSourceFactory(Map<String,Object> map,int shardIndex,ShardBy shardBy){
        String key = shardBy.value();
        if(key == null || key.isEmpty()){
            key = ":"+(shardIndex+1);
        }
        Object value = map.get(ShardInterpreter.SHARD_PREFIX+key);
        for(int i=0;i<rules.size();i++){
            Rule rule = rules.get(i);
            if(rule.match(value)){
                return dataSources.get(i);
            }
        }
        return null;
    }
}
