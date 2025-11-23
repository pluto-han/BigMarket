package org.example.domain.activity.service.quota.rule;

import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @program: BigMarket
 * @description: 下单规则过滤接口
 * @author: Hancong Zhang
 * @create: 11/21/25 1:04 PM
 **/
public interface IActionChain extends IActionArmory {
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityCountEntity activityCountEntity, ActivityEntity activityEntity);
}
