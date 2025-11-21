package org.example.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.service.rule.AbstractActionChain;
import org.springframework.stereotype.Component;

/**
 * @program: BigMarket
 * @description: 活动规则过滤 [日期/状态]
 * @author: Hancong Zhang
 * @create: 11/21/25 1:05 PM
 **/
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityCountEntity activityCountEntity, ActivityEntity activityEntity) {
        log.info("活动责任链-基础信息 【有效期、状态】 校验开始...");

        return next().action(activitySkuEntity, activityCountEntity, activityEntity);
    }
}
