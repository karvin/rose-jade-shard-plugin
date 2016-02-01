package net.paoding.rose.jade.shard.rule.matchers;

/**
 * Created by karvin on 16/1/29.
 */
public class ModMatcher implements RuleMatcher {

    private int mod;
    private int max;

    public ModMatcher(){

    }

    public ModMatcher(int max,int mod){
        this.max = max;
        this.mod = mod;
    }

    public boolean match(Object value) {
        if(value instanceof Number){
            Number number = (Number)value;
            long l = number.longValue();
            int m = (int)(l%max);
            return mod == m;
        }
        return false;
    }
}
