package org.example.domain.activity.service.quota.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.armory.IActivityDispatch;
import org.example.domain.activity.service.quota.rule.AbstractActionChain;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description: 活动规则过滤 [校验&扣减]
 * @author: Hancong Zhang
 * @create: 11/21/25 1:05 PM
 **/
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {
    @Resource
    private IActivityRepository activityRepository;

    @Resource
    private IActivityDispatch activityDispatch;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityCountEntity activityCountEntity, ActivityEntity activityEntity) {
        log.info("活动责任链-商品库存处理【有效期、状态、库存(sku)】开始。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());

        // 扣减库存
        boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());

        // 扣减成功
        if (status) {
            log.info("活动责任链-商品库存处理【有效期、状态、库存(sku)】成功。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());

            // 写入延迟队列，延迟更新库存记录
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activityEntity.getActivityId())
                    .build());

            return true;
        }

        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
    }
}
