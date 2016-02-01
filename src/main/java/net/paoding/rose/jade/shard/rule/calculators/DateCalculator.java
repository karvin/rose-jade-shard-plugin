package net.paoding.rose.jade.shard.rule.calculators;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karvin on 16/1/29.
 */
public class DateCalculator implements Calculator {

    private String formatter;

    public DateCalculator(String formatter){
        this.formatter = formatter;
    }

    public DateCalculator(){

    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public Object calculate(Object value) {
        if(value instanceof Date){
            Date date = (Date)value;
            SimpleDateFormat sdf = new SimpleDateFormat(this.getFormatter());
            return sdf.format(date);
        }
        throw new IllegalArgumentException("DateCalculator only apply on Date");
    }
}
