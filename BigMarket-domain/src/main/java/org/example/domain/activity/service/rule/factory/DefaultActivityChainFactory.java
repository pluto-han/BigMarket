package org.example.domain.activity.service.rule.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.activity.service.rule.IActionChain;
import org.example.domain.activity.service.rule.impl.ActivitySkuStockActionChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: BigMarket
 * @description: 责任链工厂默认实现
 * @author: Hancong Zhang
 * @create: 11/21/25 1:05 PM
 **/
@Service
public class DefaultActivityChainFactory {
    private final IActionChain actionChain;

    public DefaultActivityChainFactory(Map<String, IActionChain> actionChainGroup, ActivitySkuStockActionChain activitySkuStockActionChain) {
        actionChain = actionChainGroup.get("activity_base_action");
        actionChain.appendNext(actionChainGroup.get(ActionModel.activity_sku_stock_action.getCode()));
    }

    public IActionChain openActionChain() {
        return this.actionChain;
    }

    @Getter
    @AllArgsConstructor
    public enum ActionModel {
        activity_base_action("activity_base_action", "活动的时间、状态校验"),
        activity_sku_stock_action("activity_sku_stock_action", "活动sku库存校验"),
        ;

        private final String code;
        private final String info;
    }
}
