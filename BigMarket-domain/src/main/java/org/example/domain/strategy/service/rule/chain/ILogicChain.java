package org.example.domain.strategy.service.rule.chain;

import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/13/25 9:45 PM
 **/
public interface ILogicChain extends ILogicChainArmory{

    /**
     *
     * @param userId
     * @param strategyId
     * @return
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
}
