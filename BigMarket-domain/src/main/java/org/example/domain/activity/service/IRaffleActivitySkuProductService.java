package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

/**
 * @author: Hancong Zhang
 * @description: sku商品服务接口
 * @create: 12/1/25 10:53 AM
 */
public interface IRaffleActivitySkuProductService {
    /**
     * 查询当前活动ID下，创建的 sku 商品。「sku可以兑换活动抽奖次数」
     * @param activityId 活动ID
     * @return 返回sku商品集合
     */
    List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId);

}
