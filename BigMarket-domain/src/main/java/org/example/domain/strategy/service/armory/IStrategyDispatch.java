package org.example.domain.strategy.service.armory;

import java.util.Date;

/**
 * @program: BigMarket
 * @description: Strategy Lottery Dispatch
 * @author: Hancong Zhang
 * @create: 11/11/25 7:11 PM
 **/
public interface IStrategyDispatch {
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

    /**
     * 库存扣减
     * @param strategyId
     * @param awardId
     * @param endDataTime
     * @return
     */
    Boolean subtractionAwardStock(Long strategyId, Integer awardId, Date endDataTime);
}
