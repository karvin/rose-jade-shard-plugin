package net.paoding.rose.jade.shard.rule.metas;

import net.paoding.rose.jade.shard.rule.Rule;

/**
 * Created by karvin on 16/1/30.
 */
public class RuleMeta {

    private String database;
    private Rule rule;

    public RuleMeta(){

    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
