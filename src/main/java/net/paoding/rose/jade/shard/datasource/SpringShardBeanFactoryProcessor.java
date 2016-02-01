package net.paoding.rose.jade.shard.datasource;

import net.paoding.rose.jade.dataaccess.DataSourceFactory;
import net.paoding.rose.jade.dataaccess.datasource.SimpleDataSourceFactory;
import net.paoding.rose.jade.shard.io.XmlRuleParser;
import net.paoding.rose.jade.shard.rule.Rule;
import net.paoding.rose.jade.shard.rule.metas.RuleMeta;
import net.paoding.rose.jade.shard.rule.metas.RuleMetaMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by karvin on 16/1/30.
 */
public class SpringShardBeanFactoryProcessor implements BeanFactoryPostProcessor {

    private static final String ruleLocation = "jade.shard.rule.location";

    private static Log log = LogFactory.getLog(SpringShardBeanFactoryProcessor.class);

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String springFlag = System.getProperty("jade.context.spring");
        if(springFlag != null && springFlag.length()>0){
            return;
        }
        List<RuleMeta> metas = this.getRuleMetas();
        this.doPostProcessBeanFactory(metas, beanFactory);
    }

    private void doPostProcessBeanFactory(List<RuleMeta> ruleMetas,ConfigurableListableBeanFactory beanFactory){
        if(ruleMetas == null || ruleMetas.size() == 0){
            log.info("no rule exist");
            return;
        }
        Map<String,List<RuleMeta>> ruleMap = new HashMap<String, List<RuleMeta>>();
        Map<String,List<Object>> dataSourceMap = new HashMap<String, List<Object>>();
        for(RuleMeta meta:ruleMetas){
            String catalog = meta.getRule().getCatalog();
            List<RuleMeta> list = ruleMap.get(catalog);
            if(list == null){
                list = new ArrayList<RuleMeta>();
                ruleMap.put(catalog,list);
            }
            list.add(meta);
            String database = meta.getDatabase();
            Object object = beanFactory.getBean(database);
            if(!(object instanceof DataSource)&&!(object instanceof DataSourceFactory)){
                throw new IllegalArgumentException("illegal argument exception");
            }
            List<Object> dataSourceList = dataSourceMap.get(catalog);
            if(dataSourceList == null){
                dataSourceList = new ArrayList<Object>();
                dataSourceMap.put(catalog,dataSourceList);
            }
            dataSourceList.add(object);
        }
        this.registerShardDataSourceFactory(ruleMap, dataSourceMap, beanFactory);
    }

    private void registerShardDataSourceFactory(Map<String,List<RuleMeta>> ruleMetaMap,Map<String,List<Object>> dataSourceMap,ConfigurableListableBeanFactory beanFactory){
        if(ruleMetaMap == null || dataSourceMap.isEmpty())
            return;
        if(dataSourceMap == null || dataSourceMap.isEmpty())
            return;
        for(Map.Entry<String,List<RuleMeta>> entry:ruleMetaMap.entrySet()){
            List<RuleMeta> ruleMetas = entry.getValue();
            String catalog = entry.getKey();
            List<Object> dataSources = dataSourceMap.get(catalog);
            List<Rule> rules = this.getRules(ruleMetas);
            ShardDataSourceFactory shardDataSourceFactory = new ShardDataSourceFactory(this.getDataSourceFactory(dataSources),rules);
            beanFactory.registerSingleton("jade.dataSource."+catalog, shardDataSourceFactory);
        }
        RuleMetaMap metaMap = new RuleMetaMap();
        metaMap.setMaps(ruleMetaMap);
        beanFactory.registerSingleton("jade.rule.ruleMetaMap",metaMap);
    }

    private List<Rule> getRules(List<RuleMeta> metas){
        if(metas == null ||metas.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<Rule> rules = new ArrayList<Rule>();
        for(RuleMeta meta:metas){
            rules.add(meta.getRule());
        }
        return rules;
    }

    private List<DataSourceFactory> getDataSourceFactory(List<Object> dataSources){
        if(dataSources == null || dataSources.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<DataSourceFactory> factories = new ArrayList<DataSourceFactory>();
        for(Object dataSource:dataSources){
            if(dataSource instanceof DataSource) {
                factories.add(new SimpleDataSourceFactory((DataSource)dataSource));
            }else if(dataSource instanceof DataSourceFactory){
                factories.add((DataSourceFactory)dataSource);
            }
        }
        return factories;
    }

    private List<RuleMeta> getRuleMetas(){
        String ruleLoc = System.getProperty(ruleLocation);
        if(ruleLoc == null){
            throw new IllegalArgumentException(ruleLocation+" can not be null");
        }
        InputStream is = null;
        ClassPathResource resource = new ClassPathResource(ruleLoc);
        try {
            is = resource.getInputStream();
        } catch (IOException e) {

        }
        if(is == null){
            throw new IllegalArgumentException("file not found:"+ruleLoc);
        }
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(is);
            XmlRuleParser parser = new XmlRuleParser();
            return parser.parse(doc);
        } catch (DocumentException e) {
            throw new RuntimeException("failed to read xml file, location:"+ruleLoc);
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
    }

}
