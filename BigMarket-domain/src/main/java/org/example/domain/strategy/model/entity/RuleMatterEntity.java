package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: BigMarket
 * @description: rule matter entity
 * @author: Hancong Zhang
 * @create: 11/12/25 11:39 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleMatterEntity {
    /** user id */
    private String userId;
    /** strategy id */
    private Long strategyId;
    /** award id () */
    private Integer awardId;
    /** rule model (rule_random - calculate random value; rule_lock - unlock after several raffles; rule_luck_award - lucky award(Guaranteed prize)) */
    private String ruleModel;
}
