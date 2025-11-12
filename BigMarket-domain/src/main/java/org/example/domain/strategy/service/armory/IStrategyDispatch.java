package org.example.domain.strategy.service.armory;

/**
 * @program: BigMarket
 * @description: Strategy Lottery Dispatch
 * @author: Hancong Zhang
 * @create: 11/11/25 7:11 PM
 **/
public interface IStrategyDispatch {
    Integer getRandomAwardId(Long strategyId);

    Integer getRndomAwardId(Long strategyId, String ruleWeightValue);
}
