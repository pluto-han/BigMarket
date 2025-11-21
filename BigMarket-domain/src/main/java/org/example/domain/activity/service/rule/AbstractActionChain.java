package org.example.domain.activity.service.rule;

/**
 * @program: BigMarket
 * @description: 下单规则责任链抽象类
 * @author: Hancong Zhang
 * @create: 11/21/25 1:04 PM
 **/
public abstract class AbstractActionChain implements IActionChain {
    private IActionChain next;

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }

    @Override
    public IActionChain next() {
        return next;
    }
}
