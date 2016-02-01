package net.paoding.rose.jade.shard.io;

import net.paoding.rose.jade.shard.rule.DefaultRule;
import net.paoding.rose.jade.shard.rule.calculators.*;
import net.paoding.rose.jade.shard.rule.generators.AppendGenerator;
import net.paoding.rose.jade.shard.rule.generators.Generator;
import net.paoding.rose.jade.shard.rule.matchers.ModMatcher;
import net.paoding.rose.jade.shard.rule.matchers.RangeMatcher;
import net.paoding.rose.jade.shard.rule.matchers.RuleMatcher;
import net.paoding.rose.jade.shard.rule.metas.RuleMeta;
import net.paoding.rose.jade.shard.utils.RangeUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by karvin on 16/1/30.
 *xml file like the follow:
 * <rules>
 *     <rule catalog="a" database="database1" type="range" min="0" max="1000">
 *      <table name="t1" append="_" prefix="true" type="mod" max="100">
 *      </table>
 *     </rule>
 *     <rule catalog="a" database="database1" type="range" min="0" max="1000">
 *      <table name="t1" append="_" prefix="true" type="mod" max="100">
 *      </table>
 *     </rule>
 *     <rule catalog="a" database="database1" type="range" min="0" max="1000">
 *      <table name="t1" append="_" prefix="true" type="mod" max="100">
 *      </table>
 *     </rule>
 * </rules>
 */
public class XmlRuleParser {

    public List<RuleMeta> parse(Document document){

        Element root = document.getRootElement();
        if(root.getName() != "rules"){
            throw new IllegalArgumentException("root wrong");
        }
        List<Attribute> attributes = root.attributes();
        List<Element> elements = root.elements();
        return parseRule(elements);
    }

    private List<RuleMeta> parseRule(List<Element> ruleElements){
        if(ruleElements ==  null || ruleElements.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        List<RuleMeta> metas = new ArrayList<RuleMeta>();
        for(Element element:ruleElements){
            RuleMeta ruleMeta = new RuleMeta();
            String database = element.attributeValue("database");
            ruleMeta.setDatabase(database);
            String catalog = element.attributeValue("catalog");
            RuleMatcher matcher = this.getMatcher(element);
            List<Element> subs = element.elements();
            Element table = subs.get(0);
            Generator generator = this.getGenerator(table);
            DefaultRule rule = new DefaultRule();
            rule.setMatcher(matcher);
            rule.setGenerator(generator);
            rule.setCatalog(catalog);
            ruleMeta.setRule(rule);
            metas.add(ruleMeta);
        }
        return metas;
    }

    private RuleMatcher getMatcher(Element rule){
        String type = rule.attributeValue("type");
        RuleMatcher ruleMatcher = null;
        if(type.equals("range")){
            RangeMatcher rangeMatcher = new RangeMatcher();
            String min = rule.attributeValue("min");
            String max = rule.attributeValue("max");
            if(min != null ){
                int value = Integer.parseInt(min);
                rangeMatcher.setMin(value);
            }
            if(max != null){
                int value = Integer.parseInt(max);
                rangeMatcher.setMax(value);
            }
            ruleMatcher = rangeMatcher;
        }else if("mod".equals(type)){
            String max = rule.attributeValue("max");
            int value = Integer.parseInt(max);
            String mod = rule.attributeValue("mod");
            int modValue = Integer.parseInt(mod);
            ruleMatcher = new ModMatcher(value,modValue);
        }
        return ruleMatcher;
    }

    private Generator getGenerator(Element table){
        AppendGenerator generator = new AppendGenerator();
        String commonName = table.attributeValue("name");
        String append = table.attributeValue("append");
        String prefix = table.attributeValue("prefix");
        String type = table.attributeValue("type");
        if(commonName != null && !commonName.isEmpty()){
            generator.setCommonName(commonName);
        }
        if(append != null && !append.isEmpty()){
            generator.setAppendString(append);
        }
        if(prefix != null && !prefix.isEmpty()){
            generator.setPrefix(Boolean.parseBoolean(prefix));
        }
        Calculator calculator = null;
        if("mod".equals(type)){
            String max = table.attributeValue("max");
            ModCalculator modCalculator = new ModCalculator();
            modCalculator.setMax(Integer.parseInt(max));
            calculator = modCalculator;
        }else if("range".equals(type)){

            List<Element> ranges = table.elements();
            calculator = this.parseRangeCalculator(ranges);

        }else if("date".equals(type)){
            String format = table.attributeValue("format");
            DateCalculator dateCalculator = new DateCalculator(format);
            calculator = dateCalculator;
        }else if("prefix".equals(type)){
            String length = table.attributeValue("length");
            PrefixCalculator prefixCalculator = new PrefixCalculator();
            prefixCalculator.setLength(Integer.parseInt(length));
            calculator = prefixCalculator;
        }else if("suffix".equals(type)){
            String length = table.attributeValue("length");
            SuffixCalculator suffixCalculator = new SuffixCalculator();
            suffixCalculator.setLength(Integer.parseInt(length));
            calculator = suffixCalculator;
        }
        if(calculator != null){
            generator.setCalculator(calculator);
        }
        return generator;
    }

    private RangeCalculator parseRangeCalculator(List<Element> rangeElements){
        if(rangeElements == null || rangeElements.isEmpty()){
            throw new IllegalArgumentException("range should not be empty");
        }
        RangeCalculator calculator = new RangeCalculator();
        Map<RangeCalculator.Ranger,String> rangerMap = new HashMap<RangeCalculator.Ranger, String>();
        for(Element element :rangeElements){
            String value = element.attributeValue("value");
            String min = element.attributeValue("min");
            String max = element.attributeValue("max");
            RangeCalculator.Ranger ranger = new RangeCalculator.Ranger();
            if(min != null && !min.isEmpty()){
                ranger.setMin(Integer.parseInt(min));
            }else{
                ranger.setMin(RangeUtils.MIN);
            }
            if(max != null && !max.isEmpty()){
                ranger.setMax(Integer.parseInt(max));
            }else{
                ranger.setMax(RangeUtils.MAX);
            }
            rangerMap.put(ranger,value);
        }
        return calculator;
    }

}
