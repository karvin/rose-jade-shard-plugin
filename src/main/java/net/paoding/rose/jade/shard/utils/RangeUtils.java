package net.paoding.rose.jade.shard.utils;

/**
 * Created by karvin on 16/1/29.
 */
public class RangeUtils {

    public static Comparable MIN = new Comparable() {
        public int compareTo(Object o) {
            return 0;
        }
    };

    public static Comparable MAX = new Comparable() {
        public int compareTo(Object o) {
            return 0;
        }
    };

    public static boolean isInRange(Comparable comparable,Comparable min,Comparable max){
        if(max == RangeUtils.MAX && min == RangeUtils.MIN){
            return true;
        }
        if(max == RangeUtils.MAX){
            if(min.compareTo(comparable)<=0){
                return true;
            }
        }
        if(min == RangeUtils.MIN){
            if(max.compareTo(comparable)>0){
                return true;
            }
        }
        if(max.compareTo((Comparable)comparable)>0 && min.compareTo(comparable)<=0){
            return true;
        }
        return false;
    }

}
