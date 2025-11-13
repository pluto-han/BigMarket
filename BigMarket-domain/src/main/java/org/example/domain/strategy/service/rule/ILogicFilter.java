package org.example.domain.strategy.service.rule;

import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @program: BigMarket
 * @description: Logic Filter Interface
 * @author: Hancong Zhang
 * @create: 11/12/25 11:36 PM
 **/
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
