package net.paoding.rose.jade.shard.rule;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karvin on 16/1/29.
 */
public class RuleFactory {

    private Map<String,List<Rule>> ruleMap = new ConcurrentHashMap<String, List<Rule>>();

    public List<Rule> getByCatalog(String catalog){
        List<Rule> rules = ruleMap.get(catalog);
        if(rules == null){
            return Collections.EMPTY_LIST;
        }
        return rules;
    }

}
