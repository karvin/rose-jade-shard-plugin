package net.paoding.rose.jade.shard.rule.calculators;

import net.paoding.rose.jade.shard.utils.RangeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by karvin on 16/1/29.
 */
public class RangeCalculator implements Calculator {

    private Map<Ranger,String> rangerMap = new HashMap<Ranger,String>();

    public RangeCalculator(){

    }

    public Map<Ranger, String> getRangerMap() {
        return rangerMap;
    }

    public void setRangerMap(Map<Ranger, String> rangerMap) {
        this.rangerMap = rangerMap;
    }

    public Object calculate(Object value) {
        if(value instanceof Comparable) {
            for (Map.Entry<Ranger, String> entry : rangerMap.entrySet()) {
                Ranger ranger = entry.getKey();
                Comparable max = ranger.getMax();
                Comparable min = ranger.getMin();
                Comparable comparable = (Comparable)ranger;
                if(RangeUtils.isInRange(comparable,min,max)){
                    return entry.getValue();
                }
            }
        }
        throw new IllegalArgumentException("RangerCalculator only Apply on Comparable");
    }

    public static class Ranger{
        private Comparable min;
        private Comparable max;

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
}
