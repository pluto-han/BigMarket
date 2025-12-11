package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/17/25 11:01 AM
 **/
@Data
public class RaffleAwardListRequestDTO {
    // 抽奖策略ID
    @Deprecated
    private Long strategyId;
    // 活动ID
    private Long activityId;
    // 用户id
    private String userId;
}
