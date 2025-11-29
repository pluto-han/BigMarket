package org.example.domain.activity.service.quota.policy;

import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @author: Hancong Zhang
 * @description: 交易策略接口，包括；返利兑换（不用支付），积分订单（需要支付）
 * @create: 11/29/25 11:05 AM
 */
public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
