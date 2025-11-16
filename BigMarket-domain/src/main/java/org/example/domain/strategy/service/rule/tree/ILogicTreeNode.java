package org.example.domain.strategy.service.rule.tree;

import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/14/25 12:12 PM
 **/
public interface ILogicTreeNode {


    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue);


}
