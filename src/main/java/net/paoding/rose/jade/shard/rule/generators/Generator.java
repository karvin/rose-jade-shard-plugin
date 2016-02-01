package net.paoding.rose.jade.shard.rule.generators;

import net.paoding.rose.jade.shard.rule.calculators.Calculator;

/**
 * Created by karvin on 16/1/29.
 */
public interface Generator {

    String generate(Object value);

    String getCommonName();

    Calculator getCalculator();

}
