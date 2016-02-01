package net.paoding.rose.jade.shard.rule.calculators;

/**
 * Created by karvin on 16/1/29.
 */
public class ModCalculator implements Calculator {

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private int max;

    public Object calculate(Object value) {
        if(value instanceof Number){
            Number number = (Number)value;
            long l = number.longValue();
            return (int)(l%max);
        }
        throw new IllegalArgumentException("ModCalculator only apply on number");
    }
}
