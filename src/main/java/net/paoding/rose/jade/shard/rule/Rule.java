package net.paoding.rose.jade.shard.rule;

/**
 * Created by karvin on 16/1/29.
 */
public interface Rule {

    boolean match(Object value);

    String generateTableName(Object value);

    String getCatalog();

    String getCommonName();

}
