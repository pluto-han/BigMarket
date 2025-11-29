package org.example.domain.credit.repository;

import org.example.domain.credit.model.aggregate.TradeAggregate;

/**
 * @author: Hancong Zhang
 * @description: 用户积分仓储
 * @create: 11/28/25 1:38 PM
 */
public interface ICreditRepository {

    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

}
