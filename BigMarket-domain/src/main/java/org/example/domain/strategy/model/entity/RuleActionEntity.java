package org.example.domain.strategy.model.entity;

import lombok.*;
import org.example.domain.strategy.model.valobj.RuleLogicCheckTypeVO;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/13/25 12:10 AM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {
    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info =  RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        /** strategy id */
        private Long strategyId;
        /** rule weight key: used to choose certain weight when raffling */
        private String ruleWeightValueKey;
        /** award id */
        private Integer awardId;
    }

    static public class RaffleCenterEntity extends RaffleEntity {

    }
}
