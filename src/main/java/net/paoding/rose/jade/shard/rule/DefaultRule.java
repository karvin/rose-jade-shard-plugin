package net.paoding.rose.jade.shard.rule;

import net.paoding.rose.jade.shard.rule.calculators.Calculator;
import net.paoding.rose.jade.shard.rule.generators.Generator;
import net.paoding.rose.jade.shard.rule.matchers.RuleMatcher;

/**
 * Created by karvin on 16/1/29.
 */
public class DefaultRule implements Rule {

    private RuleMatcher matcher;

    private Generator generator;

    private String catalog;

    public boolean match(Object value) {
        Calculator calculator = generator.getCalculator();
        return matcher.match(calculator.calculate(value));
    }

    public String generateTableName(Object value) {
        return generator.generate(value);
    }

    public String getCatalog() {
        return catalog;
    }

    public String getCommonName() {
        return generator.getCommonName();
    }

    public void setMatcher(RuleMatcher matcher) {
        this.matcher = matcher;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
}
