package net.paoding.rose.jade.shard.rule.generators;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karvin on 16/1/29.
 */
public class AppendGenerator extends AbstractGenerator {

    private String commonName;
    private String appendString = "_";
    /**
     * if prefix is false the calculate result should be suffix
     */
    private boolean prefix=false;

    private Map<String,String> cache = new ConcurrentHashMap<String,String>();

    public String getAppendString() {
        return appendString;
    }

    public void setAppendString(String appendString) {
        this.appendString = appendString;
    }

    public Map<String, String> getCache() {
        return cache;
    }

    public void setCache(Map<String, String> cache) {
        this.cache = cache;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public void setPrefix(boolean prefix) {
        this.prefix = prefix;
    }

    public AppendGenerator(String commonName, String appendString){
        this(commonName,appendString,false);
    }

    public AppendGenerator(String commonName,String appendString,boolean prefix){
        this.commonName = commonName;
        this.appendString = appendString;
        this.prefix = prefix;
    }

    public AppendGenerator(){

    }

    public String generate(Object value) {

        String cr = String.valueOf(this.calculate(value));

        String result = this.cache.get(cr);
        if(result == null){
            if(this.isPrefix()){
                result = cr + appendString + commonName;
            }else {
                result = this.getCommonName() + appendString + cr;
            }
            cache.put(cr,result);
        }
        return result;
    }
}
