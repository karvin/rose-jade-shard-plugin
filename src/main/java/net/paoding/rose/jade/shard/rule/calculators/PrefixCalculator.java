package net.paoding.rose.jade.shard.rule.calculators;

/**
 * Created by karvin on 16/1/29.
 */
public class PrefixCalculator implements Calculator {

    private int length;

    public PrefixCalculator(){

    }

    public PrefixCalculator(int length){
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Object calculate(Object value) {
        if(this.getLength()<=0){
            throw new IllegalArgumentException("length must bigger than 0");
        }
        if(value instanceof String){
            String str = (String)value;
            if(str.length()<=this.getLength()){
                return str;
            }
            return str.substring(0,this.getLength());
        }
        throw new IllegalArgumentException("PrefixCalculator only apply on String");
    }
}
