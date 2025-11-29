package org.example.domain.credit.service;

import org.example.domain.credit.model.entity.TradeEntity;

/**
 * @author: Hancong Zhang
 * @description: 积分调额接口【正逆向，增减积分】
 * @create: 11/28/25 1:40 PM
 */
public interface ICreditAdjustService {

    /**
     * 创建增加积分额度订单
     * @param tradeEntity 交易实体对象
     * @return 单号
     */
    String createOrder(TradeEntity tradeEntity);

}
