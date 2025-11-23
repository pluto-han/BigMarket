package org.example.domain.activity.service.quota.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.valobj.ActivityStateVO;
import org.example.domain.activity.service.quota.rule.AbstractActionChain;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        log.info("活动责任链-基础信息【有效期、状态、库存(sku)】校验开始。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        // 1. 活动状态校验
        if(!ActivityStateVO.open.equals(activityEntity.getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        // 2. 活动有效期校验
        Date currentDate = new Date();
        Date beginDateTime = activityEntity.getBeginDateTime();
        Date endDateTime = activityEntity.getEndDateTime();
        if(currentDate.before(beginDateTime) || currentDate.after(endDateTime)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        // 3. 活动库存校验
        if(activitySkuEntity.getStockCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
        }

        return next().action(activitySkuEntity, activityCountEntity, activityEntity);
    }
}
