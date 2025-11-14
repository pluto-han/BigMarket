package org.example.domain.strategy.service.rule.tree.factory.engine;

import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/14/25 12:20 PM
 **/
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId);
}
