package net.paoding.rose.jade.shard.interceptor;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ShardBy;
import net.paoding.rose.jade.shard.parser.SqlParser;
import net.paoding.rose.jade.shard.rule.Rule;
import net.paoding.rose.jade.shard.rule.metas.RuleMeta;
import net.paoding.rose.jade.shard.rule.metas.RuleMetaMap;
import net.paoding.rose.jade.statement.Interpreter;
import net.paoding.rose.jade.statement.StatementRuntime;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karvin on 16/1/29.
 */
public class ShardInterpreter implements Interpreter,ApplicationContextAware {

    private static final Log logger = LogFactory.getLog(ShardInterpreter.class);

    public static final String SHARD_PREFIX = "jade.shard.";

    private ApplicationContext beanFactory;

    private Map<Class,String> catalogCache = new HashMap<Class, String>();

    private Map<String,List<Rule>> rules = new HashMap<String, List<Rule>>();

    private Map<SqlCacheKey,String> shardSqlCache = new ConcurrentHashMap<SqlCacheKey, String>();

    public void interpret(StatementRuntime runtime) {
        Map<String,Object> parameters = runtime.getParameters();
        int shardIndex = runtime.getMetaData().getShardByIndex();
        ShardBy shardBy = runtime.getMetaData().getShardBy();
        if(shardBy == null ){
            return;
        }
        String key = shardBy.value();
        if(key == null || key.isEmpty()){
            key = ":"+(shardIndex+1);
        }
        Object value = parameters.get(key);
        runtime.setAttribute(SHARD_PREFIX+key,value);
        String sql = runtime.getSQL();
        Rule rule = this.selectRule(value, this.getCatalog(runtime.getMetaData().getDAOMetaData().getDAOClass()));
        if(rule != null) {
            String tableName = rule.generateTableName(value);
            Method method = runtime.getMetaData().getMethod();
            sql = this.getSql(method,sql,rule.getCommonName(),tableName);
            runtime.setSQL(sql);
        }
    }

    private String getSql(Method method,String sql,String oldTable,String newTable){
        SqlCacheKey key = new SqlCacheKey();
        key.method = method;
        key.tableName = newTable;
        String newSql = shardSqlCache.get(key);
        if(newSql == null){
            try {
                newSql = SqlParser.generate(sql,oldTable,newTable);
                shardSqlCache.put(key,newSql);
            } catch (JSQLParserException e) {
                throw new RuntimeException("failed to parse sql:"+sql,e);
            }
        }
        return newSql;
    }

    private Rule selectRule(Object value,String catalog){
        List<Rule> list = this.rules.get(catalog);
        if(list == null){
            return null;
        }
        for(Rule rule:list){
            if(rule.match(value)){
                return rule;
            }
        }
        return null;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext;
        Map<String,RuleMetaMap> ruleMap = beanFactory.getBeansOfType(RuleMetaMap.class);
        for(Map.Entry<String,RuleMetaMap> entry:ruleMap.entrySet()){
            RuleMetaMap ruleMetaMap = entry.getValue();
            for(Map.Entry<String,List<RuleMeta>> e:ruleMetaMap.getMaps().entrySet()) {
                List<RuleMeta> metas = e.getValue();
                List<Rule> list = this.rules.get(e.getKey());
                if (list == null) {
                    list = new ArrayList<Rule>();
                    rules.put(e.getKey(), list);
                }
                list.addAll(this.getRules(metas));
            }
        }
    }

    private List<Rule> getRules(List<RuleMeta> metas){
        if(metas == null || metas.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<Rule> rules = new ArrayList<Rule>();
        for(RuleMeta meta:metas){
            rules.add(meta.getRule());
        }
        return rules;
    }

    private String getCatalog(Class daoClass){
        String catalog = catalogCache.get(daoClass);
        if(catalog == null) {
            DAO dao = (DAO) daoClass.getAnnotation(DAO.class);
            if (dao == null) {
                return null;
            }
            catalog = dao.catalog();
            catalogCache.put(daoClass,catalog);
        }
        return catalog;
    }

    private class SqlCacheKey{
        private Method method;
        private String tableName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SqlCacheKey that = (SqlCacheKey) o;

            if (method != null ? !method.equals(that.method) : that.method != null) return false;
            return !(tableName != null ? !tableName.equals(that.tableName) : that.tableName != null);

        }

        @Override
        public int hashCode() {
            int result = method != null ? method.hashCode() : 0;
            result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
            return result;
        }
    }

}
