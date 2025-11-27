package org.example.trigger.api;

import org.example.trigger.api.dto.*;
import org.example.types.model.Response;

import java.util.List;

/**
 * @program: BigMarket
 * @description: 抽奖服务接口
 * @author: Hancong Zhang
 * @create: 11/17/25 10:54 AM
 **/
public interface IRaffleStrategyService {

    /**
     * 策略装配接口
     * @param strategyId
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询抽奖奖品列表配置
     * @param request
     * @return 奖品列表数据
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO request);

    Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(RaffleStrategyRuleWeightRequestDTO request);

    Response<RaffleStrategyResponseDTO> randomRaffle(RaffleStrategyRequestDTO request);
}
