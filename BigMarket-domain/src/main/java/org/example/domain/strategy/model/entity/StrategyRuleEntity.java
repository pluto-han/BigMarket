package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.types.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: BigMarket
 * @description: StrategyRuleEntity
 * @author: Hancong Zhang
 * @create: 11/11/25 7:49 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyRuleEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖奖品ID【规则类型为策略，则不需要奖品ID】 */
    private Integer awardId;
    /** 抽象规则类型；1-策略规则、2-奖品规则 */
    private Integer ruleType;
    /** 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】 */
    private String ruleModel;
    /** 抽奖规则比值 */
    private String ruleValue;
    /** 抽奖规则描述 */
    private String ruleDesc;

    /**
     * get rule weight
     * i.e. 4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     */
    public Map<String, List<Integer>> getRuleWeightValues() {
        if (!"rule_weight".equals(ruleModel)) return null;
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> resultMap = new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            // check if input is null
            if (ruleValueGroup == null || ruleValueGroup.isEmpty()) {
                return resultMap;
            }
            // split string to get key and value

            String[] parts = ruleValueGroup.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueGroup);
            }
            // parse value
            String[] valueStrings = parts[1].split(Constants.SPLIT);
            List<Integer> values = new ArrayList<>();
            for (String valueString : valueStrings) {
                values.add(Integer.parseInt(valueString));
            }
            // put key and value into map
            resultMap.put(ruleValueGroup, values);
        }

        return resultMap;
    }

}
