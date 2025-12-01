package org.example.domain.credit.repository;

import org.example.domain.credit.model.aggregate.TradeAggregate;
import org.example.domain.credit.model.entity.CreditAccountEntity;

/**
 * @author: Hancong Zhang
 * @description: 用户积分仓储
 * @create: 11/28/25 1:38 PM
 */
public interface ICreditRepository {

    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

    CreditAccountEntity queryUserCreditAccount(String userId);
}
