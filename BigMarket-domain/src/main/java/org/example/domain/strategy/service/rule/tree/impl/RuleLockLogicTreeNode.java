package org.example.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Component;

/**
 * @program: BigMarket
 * @description: rule_lock_node
 * @author: Hancong Zhang
 * @create: 11/14/25 12:14 PM
 **/
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {
    // TODO: 用户抽奖次数，后续开发要从数据库/redis中读取
    private Long userRaffleCount = 10L;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-次数锁 userId:{} strategyId:{} awardId:{} ruleValue:{}", userId, strategyId, awardId, ruleValue);
        long raffleCount = 0L;
        try {
            raffleCount = Long.parseLong(ruleValue);
        } catch (Exception e) {
            throw new RuntimeException("规则过滤-次数锁异常 ruleValue:" + ruleValue + "配置不正确");
        }

        // 用户抽奖次数大于规则限定值，放行
        if(userRaffleCount >= raffleCount){
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }

        // 用户抽奖次数小于规则限定值，规则拦截
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
