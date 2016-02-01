package net.paoding.rose.jade.shard.rule.calculators;

/**
 * Created by karvin on 16/1/29.
 */
public class SuffixCalculator implements Calculator {

    private int length;

    public SuffixCalculator(){

    }

    public SuffixCalculator(int length){
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
            int start = str.length()-this.getLength()-1;
            return str.substring(start,str.length());
        }
        throw new IllegalArgumentException("SuffixCalculator only apply on String");
    }
}
