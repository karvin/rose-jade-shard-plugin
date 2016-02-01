package net.paoding.rose.jade.shard.rule.generators;

import net.paoding.rose.jade.shard.rule.calculators.Calculator;

/**
 * Created by karvin on 16/1/29.
 */
public abstract class AbstractGenerator implements Generator {

    private Calculator calculator;

    public Calculator getCalculator() {
        return calculator;
    }

    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    public Object calculate(Object value){
        return this.getCalculator().calculate(value);
    }

}
