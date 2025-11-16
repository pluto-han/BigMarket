package org.example.domain.strategy.service.raffle;

import org.example.domain.strategy.model.entity.*;
import org.example.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import org.example.domain.strategy.model.valobj.RuleTreeVO;
import org.example.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import org.example.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.AbstractRaffleStrategy;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Hancong Zhang
 * @description 默认的抽奖策略实现
 * @create 2025-11-12 11:46
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    public DefaultRaffleStrategy(DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory, IStrategyDispatch strategyDispatch, IStrategyRepository strategyRepository) {
        super(defaultChainFactory, defaultTreeFactory, strategyDispatch, strategyRepository);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);

        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModel(strategyId, awardId);
        if (null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }

        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型Key, 未在库表rule_tree、rule_tree_node、rule_tree_line中配置对应的规则树信息");
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return strategyRepository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        strategyRepository.updateStrategyAwardStock(strategyId, awardId);
    }
}
