package net.paoding.rose.jade.shard.rule.metas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by karvin on 16/1/31.
 */
public class RuleMetaMap {

    private Map<String,List<RuleMeta>> maps = new HashMap<String,List<RuleMeta>>();

    public Map<String, List<RuleMeta>> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, List<RuleMeta>> maps) {
        this.maps = maps;
    }
}
