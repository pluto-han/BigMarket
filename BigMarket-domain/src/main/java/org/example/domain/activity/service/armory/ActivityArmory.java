package org.example.domain.activity.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.types.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/22/25 12:34 AM
 **/
@Service
@Slf4j
public class ActivityArmory implements IActivityArmory, IActivityDispatch {
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean assembleActivitySku(Long sku) {
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        cacheActivitySkuStockCount(sku, activitySkuEntity.getStockCountSurplus());

        // 预热活动（查询时预热到缓存）
        activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 预热活动次数 （查询时预热到缓存）
        activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        return true;
    }

    /**
     * assemble activity, activity sku and activity count
     * @param activityId
     * @return
     */
    @Override
    public boolean assembleActivitySkuByActivityId(Long activityId) {
        List<ActivitySkuEntity> activitySkuEntities = activityRepository.queryRaffleActivitySkuListByActivityId(activityId);
        for (ActivitySkuEntity activitySkuEntity : activitySkuEntities) {
            // preheat sku stock count
            cacheActivitySkuStockCount(activitySkuEntity.getSku(), activitySkuEntity.getStockCountSurplus());
            // preheat activity count (preheat when querying)
            activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        }
        // preheat activity (preheat when querying)
        activityRepository.queryRaffleActivityByActivityId(activityId);

        return true;
    }

    /**
     * put sku stock count into cache
     * @param sku
     * @param stockCount
     */
    private void cacheActivitySkuStockCount(Long sku, int stockCount) {
        String cacheKy = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivitySkuStockCount(cacheKy, stockCount);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
        String cacheKy = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;

        return activityRepository.subtractionActivitySkuStock(sku, cacheKy, endDateTime);
    }
}
