package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;
import org.example.domain.activity.model.entity.SkuRechargeEntity;

/**
 * @program: BigMarket
 * @description: 抽奖活动订单接口
 * @author: Hancong Zhang
 * @create: 11/20/25 5:37 PM
 **/
public interface IRaffleActivityAccountQuotaService {
    /**
     * 创建sku充值订单，给用户增加抽奖次数
     * @return
     */
    String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity);
}
