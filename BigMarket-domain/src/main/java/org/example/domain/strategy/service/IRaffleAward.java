package org.example.domain.strategy.service;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @program: BigMarket
 * @description: 策略奖品查询接口
 * @author: Hancong Zhang
 * @create: 11/17/25 12:02 PM
 **/
public interface IRaffleAward {
    /**
     * 根据策略id查询抽奖奖品列表配置
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
