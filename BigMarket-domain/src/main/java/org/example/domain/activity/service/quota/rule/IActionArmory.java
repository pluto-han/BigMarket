package org.example.domain.activity.service.quota.rule;

/**
 * @program: BigMarket
 * @description: 抽奖动作责任链装配器
 * @author: Hancong Zhang
 * @create: 11/21/25 1:04 PM
 **/
public interface IActionArmory {
    IActionChain next();

    IActionChain appendNext(IActionChain next);
}
