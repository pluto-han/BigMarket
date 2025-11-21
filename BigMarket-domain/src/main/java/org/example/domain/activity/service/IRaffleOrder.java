package org.example.domain.activity.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;
import org.example.domain.activity.model.entity.SkuRechargeEntity;
import org.springframework.stereotype.Service;

/**
 * @program: BigMarket
 * @description: 抽奖活动订单接口
 * @author: Hancong Zhang
 * @create: 11/20/25 5:37 PM
 **/
public interface IRaffleOrder {
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity shopCartEntity);

    /**
     * 创建sku充值订单，给用户增加抽奖次数
     * @return
     */
    String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity);
}
