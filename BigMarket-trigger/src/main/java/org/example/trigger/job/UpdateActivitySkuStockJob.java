package org.example.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import org.example.domain.activity.service.IRaffleActivitySkuStockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/22/25 11:15 AM
 **/
@Slf4j
@Component()
public class UpdateActivitySkuStockJob {

    @Resource
    private IRaffleActivitySkuStockService skuStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
            if (null == activitySkuStockKeyVO) return;
            log.info("定时任务，更新活动sku库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("定时任务，更新活动sku库存失败", e);
        }
    }

}

