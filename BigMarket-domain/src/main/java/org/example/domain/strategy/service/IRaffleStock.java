package org.example.domain.strategy.service;

import org.example.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @program: BigMarket
 * @description: 抽奖完成后，对库存处理的接口，获取库存消耗队列
 * @author: Hancong Zhang
 * @create: 11/16/25 11:40 AM
 **/
public interface IRaffleStock {
    /**
     * 获取奖品库存消耗队列
     * @return 奖品库存key信息
     * @throws InterruptedException
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     * @param strategyId
     * @param awardId
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
