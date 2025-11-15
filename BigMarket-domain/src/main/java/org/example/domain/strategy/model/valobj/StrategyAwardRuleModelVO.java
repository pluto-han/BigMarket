package org.example.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/13/25 4:22 PM
 **/
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

    /**
     * 获取抽奖中规则；或者使用 lambda 表达式
     * <p>
     * List<String> ruleModelList = Arrays.stream(ruleModels.split(Constants.SPLIT))
     * .filter(DefaultLogicFactory.LogicModel::isCenter)
     * .collect(Collectors.toList());
     * return ruleModelList;
     * <p>
     * List<String> collect = Arrays.stream(ruleModelValues).filter(DefaultLogicFactory.LogicModel::isCenter).collect(Collectors.toList());
     */
}
