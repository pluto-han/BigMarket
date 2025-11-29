package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.*;

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

    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);

    ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId);

    Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId);

    /**
     * 订单出货 - 积分充值
     * @param deliveryOrderEntity 出货单实体对象
     */
    void updateOrder(DeliveryOrderEntity deliveryOrderEntity);

}
