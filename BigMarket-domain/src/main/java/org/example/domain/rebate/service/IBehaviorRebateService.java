package org.example.domain.rebate.service;

import org.example.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @author: Hancong Zhang
 * @description:
 * @create: 11/26/25 12:33 PM
 */
public interface IBehaviorRebateService {
    List<String> createRebateOrder(BehaviorEntity behaviorEntity);
}
