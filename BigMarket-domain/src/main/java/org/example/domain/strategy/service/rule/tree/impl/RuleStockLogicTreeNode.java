package org.example.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import org.example.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/14/25 12:17 PM
 **/
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {
    @Resource
    private IStrategyRepository strategyRepository;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-库存扣减 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        // 扣减库存
        Boolean status = strategyDispatch.subtractionAwardStock(strategyId, awardId);
        // 扣减成功
        if (status) {
            // 写入延迟队列，延迟消费更新数据库记录 【在trigger的job; UpdateAwardStockJob 下消费队列，更新数据库记录】
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                    .strategyId(strategyId)
                    .awardId(awardId)
                    .build());

            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue(ruleValue)
                            .build())
                    .build();
        }


        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
