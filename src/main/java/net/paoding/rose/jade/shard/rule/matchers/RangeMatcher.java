package net.paoding.rose.jade.shard.rule.matchers;

import net.paoding.rose.jade.shard.utils.RangeUtils;

/**
 * Created by karvin on 16/1/29.
 */
public class RangeMatcher implements RuleMatcher {

    private Comparable min;
    private Comparable max;

    public RangeMatcher(Comparable min,Comparable max){
        this.max = max;
        this.min = min;
    }

    public RangeMatcher(Comparable min){
        this(min, RangeUtils.MAX);
    }

    public RangeMatcher(){
        this(RangeUtils.MIN,RangeUtils.MAX);
    }

    public boolean match(Object value) {
        if(value instanceof Comparable){
            return RangeUtils.isInRange((Comparable)value,min,max);
        }
        return false;
    }

    public Comparable getMax() {
        return max;
    }

    public void setMax(Comparable max) {
        this.max = max;
    }

    public Comparable getMin() {
        return min;
    }

    public void setMin(Comparable min) {
        this.min = min;
    }
}
