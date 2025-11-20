package org.example.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.model.valobj.ActivityStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.IRaffleOrder;
import org.example.infrastructure.persistent.dao.IRaffleActivityAccountDao;
import org.example.infrastructure.persistent.dao.IRaffleActivityCountDao;
import org.example.infrastructure.persistent.dao.IRaffleActivityDao;
import org.example.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import org.example.infrastructure.persistent.po.RaffleActivity;
import org.example.infrastructure.persistent.po.RaffleActivityCount;
import org.example.infrastructure.persistent.po.RaffleActivitySku;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/20/25 6:18 PM
 **/
@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {
    @Resource
    private IRedisService redisService;

    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);

        return ActivitySkuEntity.builder()
                .activityId(raffleActivitySku.getActivityId())
                .sku(raffleActivitySku.getSku())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (null != activityEntity) {
            return activityEntity;
        }
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
        redisService.setValue(cacheKey, activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (null != activityCountEntity) {
            return activityCountEntity;
        }
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }
}
