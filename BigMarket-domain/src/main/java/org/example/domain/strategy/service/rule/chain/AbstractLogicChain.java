package org.example.domain.strategy.service.rule.chain;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/13/25 9:47 PM
 **/
public abstract class AbstractLogicChain implements ILogicChain {
    private ILogicChain next;

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    protected abstract String ruleModel();
}
