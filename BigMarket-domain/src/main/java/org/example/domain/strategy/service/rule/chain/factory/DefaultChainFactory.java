package org.example.domain.strategy.service.rule.chain.factory;

import lombok.*;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/13/25 10:19 PM
 **/
@Service
public class DefaultChainFactory {
    private final Map<String, ILogicChain> logicChainGroup;

    private final IStrategyRepository strategyRepository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository strategyRepository) {
        this.logicChainGroup = logicChainGroup;
        this.strategyRepository = strategyRepository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.getRuleModels();

        if(ruleModels == null || ruleModels.length == 0){
            return logicChainGroup.get("default");
        }

        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++){
            ILogicChain next = logicChainGroup.get(ruleModels[i]);
            current = current.appendNext(next);
        }
        current.appendNext(logicChainGroup.get("default"));

        return logicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** 抽奖奖品ID - 内部流转使用 */
        private Integer awardId;
        /**  */
        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;

        private final String code;
        private final String info;

    }

}
