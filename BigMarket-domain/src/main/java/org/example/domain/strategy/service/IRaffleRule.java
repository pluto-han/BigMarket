package org.example.domain.strategy.service;

import java.util.Map;

/**
 * @author: Hancong Zhang
 * @description: 抽奖规则接口
 * @create: 11/25/25 1:36 PM
 */
public interface IRaffleRule {
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
