package org.example.domain.strategy.service.armory;

/**
 * @program: BigMarket
 * @description: Strategy assembly library, in order to initialize strategy
 * @author: Hancong Zhang
 * @create: 11/11/25 1:43 PM
 **/
public interface IStrategyArmory {
    /**
     * assemble strategy
     * @param strategyId
     * @return boolean
     */
    boolean assembleLotteryStrategy(Long strategyId);
}
