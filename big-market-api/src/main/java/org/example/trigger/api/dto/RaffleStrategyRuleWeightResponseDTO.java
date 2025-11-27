package org.example.trigger.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/27/25 3:59 PM
 */
@Data
public class RaffleStrategyRuleWeightResponseDTO {

    private Integer ruleWeightCount;

    private Integer userActivityAccountTotalUseCount;

    private List<StrategyAward> strategyAwards;

    @Data
    public static class StrategyAward {
        private Integer awardId;
        private String awardTitle;
    }
}
