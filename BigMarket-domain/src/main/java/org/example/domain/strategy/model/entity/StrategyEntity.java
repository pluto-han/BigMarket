package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.types.common.Constants;

/**
 * @program: BigMarket
 * @description: Strategy Entity
 * @author: Hancong Zhang
 * @create: 11/11/25 7:24 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEntity {
    /** 抽奖策略ID*/
    private Long strategyId;
    /** 抽奖策略描述 */
    private String strategyDesc;
    /** 抽奖策略模型 */
    private String ruleModels;

    public String[] getRuleModels() {
        if (StringUtils.isBlank(ruleModels)) {
            return null;
        }

        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] ruleModels = this.getRuleModels();
        if (null == ruleModels || ruleModels.length == 0) {
            return null;
        }
        for (String ruleModel : ruleModels) {
            if ("rule_weight".equals(ruleModel)) {
                return ruleModel;
            }
        }

        return null;
    }
}
