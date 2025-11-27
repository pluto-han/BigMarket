package org.example.domain.rebate.service;

import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;

import java.util.List;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/26/25 12:33 PM
 */
public interface IBehaviorRebateService {
    List<String> createRebateOrder(BehaviorEntity behaviorEntity);

    List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo);
}
